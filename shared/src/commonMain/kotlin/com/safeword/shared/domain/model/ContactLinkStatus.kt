package com.safeword.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class ContactLinkStatus {
    UNLINKED,
    LINK_PENDING,
    LINKED
}
