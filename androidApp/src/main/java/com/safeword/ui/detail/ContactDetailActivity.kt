package com.safeword.ui.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.safeword.BuildConfig
import com.google.android.material.snackbar.Snackbar
import com.safeword.R
import com.safeword.databinding.ActivityContactDetailBinding
import com.safeword.shared.domain.model.Contact
import com.safeword.shared.domain.model.ContactLinkStatus
import com.safeword.shared.domain.model.PlanTier
import com.safeword.ui.ContactsActivity
import com.safeword.ui.contacts.ContactsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactDetailBinding
    private val viewModel: ContactsViewModel by viewModels()

    private var contactId: Long = -1
    private var currentContact: Contact? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactId = intent.getLongExtra(EXTRA_CONTACT_ID, -1L)
        if (contactId <= 0L) {
            finish()
            return
        }

        binding = ActivityContactDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        setupPlaceholderToggles()
        setupPrimaryActions()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contacts.collect { contacts ->
                    val contact = contacts.firstOrNull { it.id == contactId }
                    if (contact == null) {
                        Snackbar.make(binding.root, R.string.contact_detail_snackbar_missing, Snackbar.LENGTH_SHORT).show()
                        finish()
                    } else {
                        currentContact = contact
                        renderContact(contact)
                    }
                }
            }
        }
    }

    private fun setupPlaceholderToggles() {
        val comingSoon: (Boolean) -> Unit = {
            Snackbar.make(binding.root, R.string.contact_detail_toggle_coming_soon, Snackbar.LENGTH_SHORT).show()
        }
        binding.switchLocationShare.setOnCheckedChangeListener { _, _ -> comingSoon(true) }
        binding.switchEmergencyAlert.setOnCheckedChangeListener { _, _ -> comingSoon(true) }
        binding.switchCheckInAlert.setOnCheckedChangeListener { _, _ -> comingSoon(true) }
        binding.switchCamera.setOnCheckedChangeListener { _, _ -> comingSoon(true) }
    }

    private fun setupPrimaryActions() {
        binding.buttonCall.setOnClickListener { currentContact?.let { dialContact(it) } }
        binding.buttonMessage.setOnClickListener { currentContact?.let { messageContact(it) } }
        binding.buttonPing.setOnClickListener { currentContact?.let { pingContact(it) } }
        binding.buttonInvite.setOnClickListener { currentContact?.let { inviteContact(it) } }
        binding.buttonGift.setOnClickListener { currentContact?.let { giftContact(it) } }
        binding.buttonLink.setOnClickListener { currentContact?.let { linkContact(it) } }
        binding.buttonUnlink.setOnClickListener { currentContact?.let { unlinkContact(it) } }
        binding.buttonEdit.setOnClickListener { currentContact?.let { launchEditContact(it) } }
        binding.buttonDelete.setOnClickListener { currentContact?.let { confirmDelete(it) } }
    }

    private fun renderContact(contact: Contact) {
        binding.textName.text = contact.name
        binding.textEmail.isVisible = !contact.email.isNullOrBlank()
        binding.textEmail.text = contact.email.orEmpty()
        binding.textPhone.text = contact.phone
        binding.textLocation.text = getString(R.string.dashboard_action_location)

        val isLinked = contact.linkStatus == ContactLinkStatus.LINKED
        binding.textLinkStatus.text = when (contact.linkStatus) {
            ContactLinkStatus.LINKED -> getString(R.string.contact_detail_linked_badge)
            ContactLinkStatus.LINK_PENDING -> getString(R.string.action_link_pending)
            else -> getString(R.string.contact_detail_unlinked_badge)
        }

        binding.buttonLink.isVisible = contact.linkStatus != ContactLinkStatus.LINKED
        binding.buttonLink.isEnabled = contact.linkStatus == ContactLinkStatus.UNLINKED
        binding.buttonLink.text = when (contact.linkStatus) {
            ContactLinkStatus.LINK_PENDING -> getString(R.string.action_link_pending)
            else -> getString(R.string.action_link_contact)
        }
        binding.buttonUnlink.isVisible = isLinked
        binding.buttonPing.isEnabled = isLinked

        val canGift = !BuildConfig.FEATURE_ADS_ENABLED
        val showInvite = contact.linkStatus == ContactLinkStatus.UNLINKED
        val showGift = isLinked && contact.planTier == PlanTier.FREE && canGift
        binding.buttonInvite.isVisible = showInvite
        binding.buttonInvite.isEnabled = showInvite
        binding.buttonGift.isVisible = showGift
        binding.buttonGift.isEnabled = showGift
    }

    private fun dialContact(contact: Contact) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:${contact.phone}")
        }
        runCatching { startActivity(intent) }
            .onFailure {
                Snackbar.make(binding.root, R.string.contact_action_error, Snackbar.LENGTH_SHORT).show()
            }
    }

    private fun messageContact(contact: Contact) {
        val message = getString(R.string.contact_message_body, contact.name)
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:${contact.phone}")
            putExtra("sms_body", message)
        }
        runCatching { startActivity(intent) }
            .onFailure {
                Snackbar.make(binding.root, R.string.contact_action_error, Snackbar.LENGTH_SHORT).show()
            }
    }

    private fun pingContact(contact: Contact) {
        viewModel.ping(contact) { success ->
            val res = if (success) R.string.contact_ping_prompt else R.string.contact_ping_unavailable
            Snackbar.make(binding.root, getString(res, contact.name), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun inviteContact(contact: Contact) {
        viewModel.sendInvite(contact) { sent ->
            val res = if (sent) R.string.contact_invite_sent else R.string.contact_invite_failed
            Snackbar.make(binding.root, getString(res, contact.name), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun giftContact(contact: Contact) {
        if (BuildConfig.FEATURE_ADS_ENABLED) {
            Snackbar.make(binding.root, R.string.contact_detail_gift_unavailable, Snackbar.LENGTH_SHORT).show()
            return
        }
        if (contact.phone.isBlank()) {
            Snackbar.make(binding.root, R.string.contact_gift_missing_phone, Snackbar.LENGTH_SHORT).show()
            return
        }
        val message = getString(
            R.string.contact_gift_message_body,
            contact.name,
            getString(R.string.safeword_pro_play_link)
        )
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:${contact.phone}")
            putExtra("sms_body", message)
        }
        runCatching { startActivity(intent) }
            .onFailure {
                Snackbar.make(binding.root, R.string.contact_action_error, Snackbar.LENGTH_SHORT).show()
            }
            .onSuccess {
                Snackbar.make(binding.root, getString(R.string.contact_gift_prompt, contact.name), Snackbar.LENGTH_SHORT).show()
            }
    }

    private fun linkContact(contact: Contact) {
        viewModel.sendLinkRequest(contact) { sent ->
            val res = if (sent) R.string.contact_link_request_sent else R.string.contact_link_request_failed
            Snackbar.make(binding.root, getString(res, contact.name), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun unlinkContact(contact: Contact) {
        viewModel.save(contact.copy(linkStatus = ContactLinkStatus.UNLINKED))
        Snackbar.make(binding.root, R.string.contact_detail_snackbar_unlinked, Snackbar.LENGTH_SHORT).show()
    }

    private fun launchEditContact(contact: Contact) {
        val intent = Intent(this, ContactsActivity::class.java).apply {
            putExtra(ContactsActivity.EXTRA_CONTACT_ID, contact.id)
        }
        startActivity(intent)
    }

    private fun confirmDelete(contact: Contact) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_contact)
            .setMessage(getString(R.string.contact_detail_confirm_delete, contact.name))
            .setPositiveButton(R.string.delete_contact) { _, _ ->
                contact.id?.let { id ->
                    viewModel.delete(id)
                    Snackbar.make(binding.root, getString(R.string.contact_detail_snackbar_deleted, contact.name), Snackbar.LENGTH_SHORT).show()
                    finish()
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    companion object {
        private const val EXTRA_CONTACT_ID = "extra_contact_id"

        fun createIntent(context: Context, contactId: Long): Intent {
            return Intent(context, ContactDetailActivity::class.java).apply {
                putExtra(EXTRA_CONTACT_ID, contactId)
            }
        }
    }
}
