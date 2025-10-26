package com.safeword.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.core.app.ServiceCompat
import com.safeword.shared.domain.SafeWordEngine
import com.safeword.util.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VoiceRecognitionService : Service(), RecognitionListener {

    @Inject lateinit var engine: SafeWordEngine
    @Inject lateinit var notificationHelper: NotificationHelper

    private var speechRecognizer: SpeechRecognizer? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main)
    private var shouldRestart = true
    private var isListening = false

    override fun onCreate() {
        super.onCreate()
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this).apply {
            setRecognitionListener(this@VoiceRecognitionService)
        }
        startForeground(
            NotificationHelper.NOTIFICATION_ID_LISTENING,
            notificationHelper.buildListeningNotification(isActive = true)
        )
        startListening()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        shouldRestart = false
        isListening = false
        speechRecognizer?.destroy()
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startListening() {
        if (!shouldRestart || isListening) return
        val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 2500L)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 1500L)
        }
        isListening = true
        speechRecognizer?.startListening(recognizerIntent)
    }

    override fun onReadyForSpeech(params: Bundle?) = Unit
    override fun onBeginningOfSpeech() = Unit
    override fun onRmsChanged(rmsdB: Float) = Unit
    override fun onBufferReceived(buffer: ByteArray?) = Unit
    override fun onEndOfSpeech() = Unit

    override fun onError(error: Int) {
        isListening = false
        if (!shouldRestart) return
        if (error == SpeechRecognizer.ERROR_CLIENT) {
            // Client errors typically mean the recognizer is shutting down; avoid tight loops.
            serviceScope.launch {
                delay(RESTART_DELAY_MS)
                startListening()
            }
            return
        }
        serviceScope.launch {
            delay(RESTART_DELAY_MS)
            startListening()
        }
    }

    override fun onResults(results: Bundle?) {
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        matches?.firstOrNull()?.let { phrase ->
            serviceScope.launch { engine.processTranscript(phrase) }
        }
        isListening = false
        if (shouldRestart) {
            serviceScope.launch {
                delay(RESTART_DELAY_MS)
                startListening()
            }
        }
    }

    override fun onPartialResults(partialResults: Bundle?) {
        val partial = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        partial?.firstOrNull()?.let { phrase ->
            serviceScope.launch { engine.processTranscript(phrase) }
        }
    }

    override fun onEvent(eventType: Int, params: Bundle?) = Unit

    companion object {
        private const val RESTART_DELAY_MS = 1_500L

        fun start(context: Context) {
            val intent = Intent(context, VoiceRecognitionService::class.java)
            context.startForegroundService(intent)
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, VoiceRecognitionService::class.java))
        }
    }
}
