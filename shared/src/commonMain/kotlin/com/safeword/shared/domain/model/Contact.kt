package com.safeword.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val id: Long? = null,
    val name: String,
    val phone: String,
    val email: String? = null,
    val createdAtMillis: Long,
    val linkStatus: ContactLinkStatus = ContactLinkStatus.UNLINKED,
    val planTier: PlanTier? = null
) {
    val isSafewordPeer: Boolean get() = linkStatus == ContactLinkStatus.LINKED
}
