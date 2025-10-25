package com.safeword.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ContactEntity::class, AlertEventEntity::class],
    version = 1,
    exportSchema = true
)
abstract class SafeWordDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
    abstract fun alertEventDao(): AlertEventDao
}

