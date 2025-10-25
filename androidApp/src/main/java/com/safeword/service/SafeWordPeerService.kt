package com.safeword.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import com.safeword.shared.bridge.PeerBridge
import com.safeword.shared.bridge.model.PeerBridgeState
import com.safeword.util.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class SafeWordPeerService : Service() {

    @Inject lateinit var peerBridge: PeerBridge
    @Inject lateinit var notificationHelper: NotificationHelper

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate() {
        super.onCreate()
        val initial = notificationHelper.buildPeerNotification("Starting peer bridge")
        startForeground(NotificationHelper.NOTIFICATION_ID_PEER, initial)
        scope.launch {
            peerBridge.state.collectLatest { state ->
                val description = when (state) {
                    is PeerBridgeState.Connected -> "Connected to ${state.peerCount} peers"
                    is PeerBridgeState.Error -> state.message
                    PeerBridgeState.Idle -> "Peer bridge idle"
                }
                NotificationManagerCompat.from(this@SafeWordPeerService).notify(
                    NotificationHelper.NOTIFICATION_ID_PEER,
                    notificationHelper.buildPeerNotification(description)
                )
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scope.launch { peerBridge.startAdvertising() }
        return START_STICKY
    }

    override fun onDestroy() {
        runBlocking { runCatching { peerBridge.stop() } }
        scope.cancel()
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        fun start(context: Context) {
            context.startForegroundService(Intent(context, SafeWordPeerService::class.java))
        }
    }
}
