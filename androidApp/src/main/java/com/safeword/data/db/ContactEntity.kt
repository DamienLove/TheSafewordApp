package com.safeword.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val phone: String,
    val email: String?,
    val createdAt: Long,
    val safewordPeer: Boolean,
    val planTier: String?
)
