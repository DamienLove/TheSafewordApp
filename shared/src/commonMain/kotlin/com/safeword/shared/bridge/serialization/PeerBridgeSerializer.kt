package com.safeword.shared.bridge.serialization

import com.safeword.shared.bridge.model.PeerBridgeEvent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object PeerBridgeSerializer {
    private val json = Json {
        prettyPrint = false
        ignoreUnknownKeys = true
        classDiscriminator = "type"
    }

    fun encode(event: PeerBridgeEvent): ByteArray =
        json.encodeToString(event).encodeToByteArray()

    @Suppress("TooGenericExceptionCaught")
    fun decode(raw: ByteArray): PeerBridgeEvent? {
        return try {
            json.decodeFromString<PeerBridgeEvent>(raw.decodeToString())
        } catch (t: Throwable) {
            null
        }
    }
}

