package com.safeword.service

import android.content.Context
import android.net.wifi.WifiManager
import com.safeword.shared.bridge.PeerBridge
import com.safeword.shared.bridge.model.PeerBridgeEvent
import com.safeword.shared.bridge.model.PeerBridgeState
import com.safeword.shared.bridge.serialization.PeerBridgeSerializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket

class AndroidPeerBridge(
    private val context: Context,
    private val scope: CoroutineScope
) : PeerBridge {

    private val stateFlow = MutableStateFlow<PeerBridgeState>(PeerBridgeState.Idle)
    private val eventsFlow = MutableSharedFlow<PeerBridgeEvent>(
        replay = 0,
        extraBufferCapacity = 16,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val state: Flow<PeerBridgeState> = stateFlow.asStateFlow()
    override val incomingEvents: Flow<PeerBridgeEvent> = eventsFlow.asSharedFlow()

    private var listenJob: Job? = null
    private var multicastLock: WifiManager.MulticastLock? = null

    override suspend fun startAdvertising() {
        if (listenJob != null) return
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        multicastLock = wifiManager.createMulticastLock("SafeWordPeer").apply { setReferenceCounted(true); acquire() }
        listenJob = scope.launch(Dispatchers.IO) {
            stateFlow.value = PeerBridgeState.Connected(peerCount = 0)
            runListener()
        }
    }

    override suspend fun stop() {
        listenJob?.cancel()
        listenJob = null
        multicastLock?.release()
        multicastLock = null
        stateFlow.value = PeerBridgeState.Idle
    }

    override suspend fun broadcast(event: PeerBridgeEvent.AlertBroadcast) {
        withContext(Dispatchers.IO) {
            runCatching {
                MulticastSocket().use { socket ->
                    val payload = PeerBridgeSerializer.encode(event)
                    val packet = DatagramPacket(
                        payload,
                        payload.size,
                        InetAddress.getByName(MULTICAST_ADDRESS),
                        MULTICAST_PORT
                    )
                    socket.send(packet)
                }
            }.onFailure {
                stateFlow.value = PeerBridgeState.Error(it.message ?: "peer send error")
            }
        }
    }

    private suspend fun runListener() {
        withContext(Dispatchers.IO) {
            val group = InetAddress.getByName(MULTICAST_ADDRESS)
            MulticastSocket(MULTICAST_PORT).use { socket ->
                socket.joinGroup(group)
                val buffer = ByteArray(4096)
                while (scope.isActive) {
                    try {
                        val packet = DatagramPacket(buffer, buffer.size)
                        socket.receive(packet)
                        val data = packet.data.copyOf(packet.length)
                        val event = PeerBridgeSerializer.decode(data) ?: continue
                        eventsFlow.emit(event)
                    } catch (t: Throwable) {
                        stateFlow.value = PeerBridgeState.Error(t.message ?: "peer listen error")
                    }
                }
                socket.leaveGroup(group)
            }
        }
    }

    companion object {
        private const val MULTICAST_ADDRESS = "224.1.1.29"
        private const val MULTICAST_PORT = 45230
    }
}
