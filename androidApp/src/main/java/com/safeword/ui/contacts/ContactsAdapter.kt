package com.safeword.ui.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.safeword.databinding.ItemContactBinding
import com.safeword.shared.domain.model.Contact
import com.safeword.shared.domain.model.PlanTier
import com.safeword.shared.domain.model.ContactLinkStatus

class ContactsAdapter(
    private val onEdit: (Contact) -> Unit,
    private val onDelete: (Contact) -> Unit,
    private val onCall: (Contact) -> Unit,
    private val onMessage: (Contact) -> Unit,
    private val onPing: (Contact) -> Unit,
    private val onLink: (Contact) -> Unit,
    private val onInvite: (Contact) -> Unit,
    private val onGift: (Contact) -> Unit,
    private val canGift: Boolean
) : ListAdapter<Contact, ContactsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            binding.textName.text = contact.name
            binding.textPhone.text = contact.phone
            binding.imagePeerBadge.isVisible = contact.linkStatus == ContactLinkStatus.LINKED
            if (contact.email.isNullOrBlank()) {
                binding.textEmail.visibility = View.GONE
            } else {
                binding.textEmail.visibility = View.VISIBLE
                binding.textEmail.text = contact.email
            }
            binding.root.setOnClickListener { onEdit(contact) }
            binding.root.setOnLongClickListener {
                onDelete(contact)
                true
            }
            binding.buttonCall.setOnClickListener { onCall(contact) }
            binding.buttonMessage.setOnClickListener { onMessage(contact) }
            binding.buttonPing.setOnClickListener { onPing(contact) }

            when (contact.linkStatus) {
                ContactLinkStatus.UNLINKED -> {
                    binding.buttonLink.isVisible = true
                    binding.buttonLink.isEnabled = true
                    binding.buttonLink.text = binding.buttonLink.context.getString(com.safeword.R.string.action_link_contact)
                    binding.buttonPing.isEnabled = false
                }

                ContactLinkStatus.LINK_PENDING -> {
                    binding.buttonLink.isVisible = true
                    binding.buttonLink.isEnabled = false
                    binding.buttonLink.text = binding.buttonLink.context.getString(com.safeword.R.string.action_link_pending)
                    binding.buttonPing.isEnabled = false
                }

                ContactLinkStatus.LINKED -> {
                    binding.buttonLink.isVisible = false
                    binding.buttonPing.isEnabled = true
                }
            }
            binding.buttonLink.setOnClickListener { onLink(contact) }

            val isLinked = contact.linkStatus == ContactLinkStatus.LINKED
            val showInvite = contact.linkStatus == ContactLinkStatus.UNLINKED
            val showGift = canGift && isLinked && contact.planTier == PlanTier.FREE
            binding.buttonInvite.isVisible = showInvite
            binding.buttonGift.isVisible = showGift
            binding.layoutSecondaryActions.isVisible = showInvite || showGift
            binding.buttonInvite.setOnClickListener { onInvite(contact) }
            binding.buttonGift.setOnClickListener { onGift(contact) }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean = oldItem == newItem
    }
}

