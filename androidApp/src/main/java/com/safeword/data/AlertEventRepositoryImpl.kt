package com.safeword.data

import com.safeword.data.db.AlertEventDao
import com.safeword.data.db.AlertEventEntity
import com.safeword.shared.domain.model.AlertEvent
import com.safeword.shared.domain.model.AlertSource
import com.safeword.shared.domain.repository.AlertEventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AlertEventRepositoryImpl(
    private val dao: AlertEventDao
) : AlertEventRepository {

    override fun observeLatest(limit: Int): Flow<List<AlertEvent>> =
        dao.observeLatest(limit).map { list -> list.map { it.toDomain() } }

    override suspend fun record(event: AlertEvent): AlertEvent {
        val entity = event.toEntity()
        val id = dao.insert(entity)
        return event.copy(id = if (id == 0L) event.id else id)
    }

    private fun AlertEventEntity.toDomain(): AlertEvent = AlertEvent(
        id = id,
        source = AlertSource.valueOf(source),
        detectedWord = detectedWord,
        timestampMillis = timestamp,
        locationLat = locationLat,
        locationLon = locationLon,
        smsSent = smsSent,
        contactsNotified = contactsNotified
    )

    private fun AlertEvent.toEntity(): AlertEventEntity = AlertEventEntity(
        id = id ?: 0,
        source = source.name,
        detectedWord = detectedWord,
        timestamp = timestampMillis,
        locationLat = locationLat,
        locationLon = locationLon,
        smsSent = smsSent,
        contactsNotified = contactsNotified
    )
}

