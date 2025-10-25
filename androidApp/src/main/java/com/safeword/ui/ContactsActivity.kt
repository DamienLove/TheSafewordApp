package com.safeword.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
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
        val dialogBinding = DialogContactBinding.inflate(LayoutInflater.from(this))
        dialogBinding.inputName.setText(existing?.name.orEmpty())
        dialogBinding.inputPhone.setText(existing?.phone.orEmpty())
        dialogBinding.inputEmail.setText(existing?.email.orEmpty())

        AlertDialog.Builder(this)
            .setTitle(if (existing == null) getString(R.string.add_contact) else getString(R.string.edit_contact))
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.save) { dialog, _ ->
                val name = dialogBinding.inputName.text?.toString().orEmpty()
                val phone = dialogBinding.inputPhone.text?.toString().orEmpty()
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
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
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
