package com.safeword.shared.ios.bridge

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
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.UIntVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.refTo
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.sizeOf
import platform.posix.AF_INET
import platform.posix.INADDR_ANY
import platform.posix.IPPROTO_IP
import platform.posix.IPPROTO_UDP
import platform.posix.IP_ADD_MEMBERSHIP
import platform.posix.SOCK_DGRAM
import platform.posix.SOL_SOCKET
import platform.posix.SO_REUSEADDR
import platform.posix.bind
import platform.posix.close
import platform.posix.htons
import platform.posix.inet_addr
import platform.posix.ip_mreq
import platform.posix.recvfrom
import platform.posix.sendto
import platform.posix.setsockopt
import platform.posix.sockaddr
import platform.posix.sockaddr_in
import platform.posix.socket

class IosUdpPeerBridge(
    private val scope: CoroutineScope
) : PeerBridge {

    private val stateFlow = MutableStateFlow<PeerBridgeState>(PeerBridgeState.Idle)
    private val eventsFlow = MutableSharedFlow<PeerBridgeEvent>(
        replay = 0,
        extraBufferCapacity = 16,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private var listenJob: Job? = null

    override val state: Flow<PeerBridgeState> = stateFlow.asStateFlow()
    override val incomingEvents: Flow<PeerBridgeEvent> = eventsFlow.asSharedFlow()

    override suspend fun startAdvertising() {
        if (listenJob?.isActive == true) return
        listenJob = scope.launch(Dispatchers.Default) { listenLoop() }
        stateFlow.value = PeerBridgeState.Connected(peerCount = 0)
    }

    override suspend fun stop() {
        listenJob?.cancel()
        listenJob = null
        stateFlow.value = PeerBridgeState.Idle
    }

    override suspend fun broadcast(event: PeerBridgeEvent) {
        withContext(Dispatchers.Default) {
            memScoped {
                val socketFd = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP)
                if (socketFd < 0) {
                    stateFlow.value = PeerBridgeState.Error("socket send failed")
                    return@memScoped
                }
                try {
                    val payload = PeerBridgeSerializer.encode(event)
                    val addr = alloc<sockaddr_in>().apply {
                        sin_family = AF_INET.convert()
                        sin_port = htons(MULTICAST_PORT)
                        sin_addr.s_addr = inet_addr(MULTICAST_ADDRESS)
                    }
                    sendto(
                        socketFd,
                        payload.refTo(0),
                        payload.size.convert(),
                        0,
                        addr.ptr.reinterpret(),
                        sizeOf<sockaddr_in>().convert()
                    )
                } finally {
                    close(socketFd)
                }
            }
        }
    }

    private suspend fun listenLoop() {
        withContext(Dispatchers.Default) {
            memScoped {
                val socketFd = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP)
                if (socketFd < 0) {
                    stateFlow.value = PeerBridgeState.Error("socket create failed")
                    return@memScoped
                }
                try {
                    val reuse = alloc<IntVar>().apply { value = 1 }
                    setsockopt(
                        socketFd,
                        SOL_SOCKET,
                        SO_REUSEADDR,
                        reuse.ptr.reinterpret(),
                        sizeOf<IntVar>().convert()
                    )
                    val addr = alloc<sockaddr_in>().apply {
                        sin_family = AF_INET.convert()
                        sin_port = htons(MULTICAST_PORT)
                        sin_addr.s_addr = INADDR_ANY
                    }
                    if (bind(socketFd, addr.ptr.reinterpret(), sizeOf<sockaddr_in>().convert()) != 0) {
                        stateFlow.value = PeerBridgeState.Error("bind failed")
                        return@memScoped
                    }
                    val mreq = alloc<ip_mreq>().apply {
                        imr_multiaddr.s_addr = inet_addr(MULTICAST_ADDRESS)
                        imr_interface.s_addr = INADDR_ANY
                    }
                    setsockopt(
                        socketFd,
                        IPPROTO_IP,
                        IP_ADD_MEMBERSHIP,
                        mreq.ptr.reinterpret(),
                        sizeOf<ip_mreq>().convert()
                    )

                    val buffer = ByteArray(BUFFER_SIZE)
                    val srcLen = alloc<UIntVar>()
                    val srcSock = alloc<sockaddr>()
                    while (scope.isActive) {
                        srcLen.value = sizeOf<sockaddr>().convert()
                        val received = recvfrom(
                            socketFd,
                            buffer.refTo(0),
                            buffer.size.convert(),
                            0,
                            srcSock.ptr,
                            srcLen.ptr
                        )
                        if (received <= 0) continue
                        val data = buffer.copyOf(received)
                        val event = PeerBridgeSerializer.decode(data) ?: continue
                        eventsFlow.emit(event)
                    }
                } finally {
                    close(socketFd)
                }
            }
        }
    }

    companion object {
        private const val MULTICAST_ADDRESS = "224.1.1.29"
        private const val BUFFER_SIZE = 4096
        private val MULTICAST_PORT: UShort = 45230u
    }
}
