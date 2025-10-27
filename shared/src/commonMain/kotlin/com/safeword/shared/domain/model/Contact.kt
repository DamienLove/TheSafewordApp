package com.safeword.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val id: Long? = null,
    val name: String,
    val phone: String,
    val email: String? = null,
    val createdAtMillis: Long,
    val isSafewordPeer: Boolean = false
)
