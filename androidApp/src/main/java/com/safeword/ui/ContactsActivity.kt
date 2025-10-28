package com.safeword.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.safeword.BuildConfig
import com.safeword.R
import com.safeword.databinding.ActivityContactsBinding
import com.safeword.databinding.DialogContactBinding
import com.safeword.databinding.DialogMessageSignalBinding
import com.safeword.shared.domain.model.Contact
import com.safeword.shared.domain.model.ContactEngagementType
import com.safeword.shared.domain.model.ContactLinkStatus
import com.safeword.ui.contacts.ContactsAdapter
import com.safeword.ui.contacts.ContactsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactsBinding
    private val viewModel: ContactsViewModel by viewModels()
    private lateinit var adapter: ContactsAdapter

    private var pendingDialogBinding: DialogContactBinding? = null

    private val pickContactLauncher = registerForActivityResult(ActivityResultContracts.PickContact()) { uri ->
        val binding = pendingDialogBinding ?: return@registerForActivityResult
        if (uri != null) {
            importContact(uri, binding)
        } else {
            Snackbar.make(this.binding.root, R.string.contact_import_cancelled, Snackbar.LENGTH_SHORT).show()
        }
    }

    private val contactsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        pendingDialogBinding?.let { binding ->
            if (granted) {
                launchContactPicker(binding)
            } else {
                Snackbar.make(this.binding.root, R.string.contact_permission_denied, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.contacts_title)

        adapter = ContactsAdapter(
            onEdit = { contact -> showContactDialog(contact) },
            onDelete = { contact -> deleteContact(contact) },
            onCall = { contact -> callContact(contact) },
            onMessage = { contact -> messageContact(contact) },
            onPing = { contact -> pingContact(contact) },
            onLink = { contact -> linkContact(contact) },
            onInvite = { contact -> inviteContact(contact) },
            onGift = { contact -> giftContact(contact) },
            canGift = !BuildConfig.FEATURE_ADS_ENABLED
        )
        binding.listContacts.layoutManager = LinearLayoutManager(this)
        binding.listContacts.adapter = adapter

        binding.fabAddContact.setOnClickListener { showContactDialog(null) }

        observeContacts()
    }

    private fun observeContacts() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contacts.collect { adapter.submitList(it) }
            }
        }
    }

    private fun showContactDialog(existing: Contact?) {
        if (!canAddAnotherContact(existing)) return
        val dialogBinding = DialogContactBinding.inflate(LayoutInflater.from(this))
        pendingDialogBinding = dialogBinding
        dialogBinding.inputName.setText(existing?.name.orEmpty())
        dialogBinding.inputPhone.setText(existing?.phone.orEmpty())
        dialogBinding.inputEmail.setText(existing?.email.orEmpty())
        dialogBinding.switchSafewordPeer.isChecked = existing?.linkStatus == ContactLinkStatus.LINKED
        dialogBinding.switchSafewordPeer.isEnabled = false
        dialogBinding.textTitle.text = if (existing == null) {
            getString(R.string.add_contact)
        } else {
            getString(R.string.edit_contact)
        }
        dialogBinding.buttonImport.setOnClickListener { launchContactPickerWithPermission(dialogBinding) }

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.save) { dialog, _ ->
                val name = dialogBinding.inputName.text?.toString().orEmpty()
                val rawPhone = dialogBinding.inputPhone.text?.toString().orEmpty()
                val phone = PhoneNumberUtils.normalizeNumber(rawPhone) ?: rawPhone
                val email = dialogBinding.inputEmail.text?.toString()
                val linkStatus = existing?.linkStatus ?: ContactLinkStatus.UNLINKED
                val contact = Contact(
                    id = existing?.id,
                    name = name,
                    phone = phone,
                    email = email,
                    createdAtMillis = existing?.createdAtMillis ?: System.currentTimeMillis(),
                    linkStatus = linkStatus,
                    planTier = existing?.planTier
                )
                viewModel.save(contact)
                dialog.dismiss()
                pendingDialogBinding = null
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                pendingDialogBinding = null
            }
            .setOnDismissListener { pendingDialogBinding = null }
            .show()
    }

    private fun canAddAnotherContact(existing: Contact?): Boolean {
        if (existing != null) return true
        val limit = BuildConfig.CONTACT_LIMIT
        if (limit <= 0) return true
        if (adapter.currentList.size >= limit) {
            Snackbar.make(
                binding.root,
                getString(R.string.contact_limit_reached, limit),
                Snackbar.LENGTH_LONG
            ).show()
            return false
        }
        return true
    }

    private fun launchContactPickerWithPermission(dialogBinding: DialogContactBinding) {
        val hasPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
        pendingDialogBinding = dialogBinding
        if (hasPermission) {
            launchContactPicker(dialogBinding)
        } else {
            contactsPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    private fun launchContactPicker(binding: DialogContactBinding) {
        pendingDialogBinding = binding
        pickContactLauncher.launch(null)
    }

    private fun importContact(uri: Uri, dialogBinding: DialogContactBinding) {
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME
        )
        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (!cursor.moveToFirst()) {
                Snackbar.make(binding.root, R.string.contact_import_failed, Snackbar.LENGTH_SHORT).show()
                return
            }
            val contactId = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
            val displayName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
            val phoneCursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID}=?",
                arrayOf(contactId.toString()),
                null
            )
            phoneCursor?.use { phones ->
                if (phones.moveToFirst()) {
                    val number = phones.getString(
                        phones.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    )
                    val normalisedNumber = PhoneNumberUtils.normalizeNumber(number) ?: number
                    dialogBinding.inputName.setText(displayName)
                    dialogBinding.inputPhone.setText(normalisedNumber)
                    Snackbar.make(binding.root, R.string.contact_import_success, Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(binding.root, R.string.contact_import_no_number, Snackbar.LENGTH_SHORT).show()
                }
            } ?: Snackbar.make(binding.root, R.string.contact_import_failed, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun deleteContact(contact: Contact) {
        contact.id?.let {
            viewModel.delete(it)
            Snackbar.make(
                binding.root,
                getString(R.string.contact_deleted, contact.name),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun callContact(contact: Contact) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:${contact.phone}")
        }
        if (contact.linkStatus == ContactLinkStatus.LINKED) {
            showContactActionDialog(contact, ContactEngagementType.CALL) { _ ->
                launchContactIntent(intent)
            }
        } else {
            launchContactIntent(intent)
            Snackbar.make(binding.root, R.string.contact_signal_unlinked_hint, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun messageContact(contact: Contact) {
        if (contact.linkStatus == ContactLinkStatus.LINKED) {
            showMessageSignalDialog(contact)
        } else {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:${contact.phone}")
                putExtra("sms_body", getString(R.string.contact_message_body, contact.name))
            }
            launchContactIntent(intent)
            Snackbar.make(binding.root, R.string.contact_signal_unlinked_hint, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun pingContact(contact: Contact) {
        viewModel.ping(contact) { success ->
            val message = if (success) {
                getString(R.string.contact_ping_prompt, contact.name)
            } else {
                getString(R.string.contact_ping_unavailable)
            }
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun inviteContact(contact: Contact) {
        viewModel.sendInvite(contact) { sent ->
            val message = if (sent) {
                getString(R.string.contact_invite_sent, contact.name)
            } else {
                getString(R.string.contact_invite_failed, contact.name)
            }
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun linkContact(contact: Contact) {
        when (contact.linkStatus) {
            ContactLinkStatus.LINKED -> {
                Snackbar.make(binding.root, getString(R.string.contact_already_linked, contact.name), Snackbar.LENGTH_SHORT).show()
                return
            }
            ContactLinkStatus.LINK_PENDING -> {
                Snackbar.make(binding.root, getString(R.string.contact_link_request_pending, contact.name), Snackbar.LENGTH_SHORT).show()
                return
            }
            ContactLinkStatus.UNLINKED -> Unit
        }
        viewModel.sendLinkRequest(contact) { sent ->
            val messageRes = if (sent) {
                R.string.contact_link_request_sent
            } else {
                R.string.contact_link_request_failed
            }
            Snackbar.make(binding.root, getString(messageRes, contact.name), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun giftContact(contact: Contact) {
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
        launchContactIntent(intent)
        Snackbar.make(binding.root, getString(R.string.contact_gift_prompt, contact.name), Snackbar.LENGTH_SHORT).show()
    }

    private fun showContactActionDialog(
        contact: Contact,
        type: ContactEngagementType,
        onProceed: (Boolean) -> Unit
    ) {
        var selectedIndex = 0
        val options = arrayOf(
            getString(R.string.contact_action_mode_emergency),
            getString(R.string.contact_action_mode_non_emergency)
        )
        val title = if (type == ContactEngagementType.CALL) {
            getString(R.string.contact_action_call_title, contact.name)
        } else {
            getString(R.string.contact_action_message_title, contact.name)
        }
        AlertDialog.Builder(this)
            .setTitle(title)
            .setSingleChoiceItems(options, selectedIndex) { _, which ->
                selectedIndex = which
            }
            .setPositiveButton(R.string.contact_action_proceed) { dialog, _ ->
                val emergency = selectedIndex == 0
                viewModel.sendContactSignal(contact, type, emergency, null) { sent ->
                    val feedback = if (sent) {
                        getString(R.string.contact_signal_sent, contact.name)
                    } else {
                        getString(R.string.contact_signal_not_sent, contact.name)
                    }
                    Snackbar.make(binding.root, feedback, Snackbar.LENGTH_SHORT).show()
                    onProceed(emergency)
                }
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun showMessageSignalDialog(contact: Contact) {
        val dialogBinding = DialogMessageSignalBinding.inflate(LayoutInflater.from(this))
        var emergencySelected = true
        val updateMessageVisibility = {
            dialogBinding.inputLayoutMessage.isVisible = !emergencySelected
            if (emergencySelected) {
                dialogBinding.inputLayoutMessage.error = null
                dialogBinding.inputMessage.setText("")
            }
        }
        dialogBinding.radioEmergency.isChecked = true
        dialogBinding.radioNonEmergency.isChecked = false
        dialogBinding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            emergencySelected = checkedId == dialogBinding.radioEmergency.id
            updateMessageVisibility()
        }
        updateMessageVisibility()

        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.contact_action_message_title, contact.name))
            .setView(dialogBinding.root)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.contact_action_send, null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
                val message = dialogBinding.inputMessage.text?.toString()?.trim().orEmpty()
                if (!emergencySelected && message.isBlank()) {
                    dialogBinding.inputLayoutMessage.error = getString(R.string.contact_message_required)
                    return@setOnClickListener
                }
                dialogBinding.inputLayoutMessage.error = null
                val payloadMessage = if (emergencySelected) null else message
                viewModel.sendContactSignal(contact, ContactEngagementType.TEXT, emergencySelected, payloadMessage) { sent ->
                    val feedback = if (sent) {
                        getString(R.string.contact_signal_sent, contact.name)
                    } else {
                        getString(R.string.contact_signal_not_sent, contact.name)
                    }
                    Snackbar.make(binding.root, feedback, Snackbar.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun launchContactIntent(intent: Intent) {
        runCatching { startActivity(intent) }
            .onFailure {
                Snackbar.make(binding.root, R.string.contact_action_error, Snackbar.LENGTH_SHORT).show()
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
