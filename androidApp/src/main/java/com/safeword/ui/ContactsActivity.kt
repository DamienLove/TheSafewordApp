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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.safeword.BuildConfig
import com.safeword.R
import com.safeword.databinding.ActivityContactsBinding
import com.safeword.databinding.DialogContactBinding
import com.safeword.shared.domain.model.Contact
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
            onMessage = { contact -> messageContact(contact) }
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
                val contact = Contact(
                    id = existing?.id,
                    name = name,
                    phone = phone,
                    email = email,
                    createdAtMillis = existing?.createdAtMillis ?: System.currentTimeMillis()
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
        launchContactIntent(intent)
    }

    private fun messageContact(contact: Contact) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:${contact.phone}")
            putExtra("sms_body", getString(R.string.contact_message_body, contact.name))
        }
        launchContactIntent(intent)
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
