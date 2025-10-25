package com.safeword.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alert_events")
data class AlertEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val source: String,
    val detectedWord: String,
    val timestamp: Long,
    val locationLat: Double?,
    val locationLon: Double?,
    val smsSent: Boolean,
    val contactsNotified: Int
)

