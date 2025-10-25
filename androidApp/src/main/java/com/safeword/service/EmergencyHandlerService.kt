package com.safeword.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.ServiceCompat
import com.safeword.shared.domain.SafeWordEngine
import com.safeword.shared.domain.model.AlertSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EmergencyHandlerService : Service() {

    @Inject lateinit var engine: SafeWordEngine

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val word = intent?.getStringExtra(EXTRA_WORD) ?: return START_NOT_STICKY
        val sourceString = intent.getStringExtra(EXTRA_SOURCE) ?: AlertSource.VOICE.name
        val source = runCatching { AlertSource.valueOf(sourceString) }.getOrDefault(AlertSource.VOICE)
        scope.launch {
            engine.triggerManual(word, source)
            stopSelf(startId)
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_DETACH)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val EXTRA_WORD = "extra_word"
        private const val EXTRA_SOURCE = "extra_source"

        fun trigger(context: Context, detectedWord: String, source: AlertSource) {
            val intent = Intent(context, EmergencyHandlerService::class.java).apply {
                putExtra(EXTRA_WORD, detectedWord)
                putExtra(EXTRA_SOURCE, source.name)
            }
            context.startService(intent)
        }
    }
}
