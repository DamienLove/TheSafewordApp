package com.safeword.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.safeword.R
import com.safeword.databinding.ActivityMainBinding
import com.safeword.service.SafeWordPeerService
import com.safeword.service.VoiceRecognitionService
import com.safeword.ui.main.MainUiState
import com.safeword.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private var updatingModeSelection = false

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val granted = result.values.all { it }
        if (granted) {
            VoiceRecognitionService.start(this)
        } else {
            binding.switchListening.isChecked = false
            Snackbar.make(binding.root, R.string.enable_permissions, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        SafeWordPeerService.start(this)

        binding.switchListening.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setListening(isChecked)
            if (isChecked) ensureVoicePermission() else VoiceRecognitionService.stop(this)
        }

        binding.modeToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked || updatingModeSelection) return@addOnButtonCheckedListener
            val incoming = checkedId == binding.buttonModeIncoming.id
            viewModel.setModeIncoming(incoming)
        }

        binding.cardSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.cardContacts.setOnClickListener {
            startActivity(Intent(this, ContactsActivity::class.java))
        }

        binding.cardSafeWords.setOnClickListener {
            startActivity(Intent(this, SafeWordWizardActivity::class.java))
        }

        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { renderState(it) }
            }
        }
    }

    private fun renderState(state: MainUiState) {
        if (binding.switchListening.isChecked != state.listeningEnabled) {
            binding.switchListening.isChecked = state.listeningEnabled
        }
        binding.textListeningStatus.text = if (state.listeningEnabled) {
            binding.switchListening.text = getString(R.string.main_listening_active)
            getString(R.string.main_listening_active)
        } else {
            binding.switchListening.text = getString(R.string.main_listening_inactive)
            getString(R.string.main_listening_inactive)
        }
        binding.textListeningSubtitle.text = if (state.listeningEnabled) {
            getString(R.string.main_listening_subtitle_on)
        } else {
            getString(R.string.main_listening_subtitle_off)
        }
        updatingModeSelection = true
        if (state.incomingMode) {
            binding.modeToggleGroup.check(binding.buttonModeIncoming.id)
        } else {
            binding.modeToggleGroup.check(binding.buttonModeOutgoing.id)
        }
        updatingModeSelection = false
        binding.textContacts.text = resources.getQuantityString(R.plurals.contacts_count, state.contacts, state.contacts)
        binding.textPeerState.text = when (state.peerState) {
            is com.safeword.shared.bridge.model.PeerBridgeState.Connected -> getString(R.string.peer_connected)
            is com.safeword.shared.bridge.model.PeerBridgeState.Error -> state.peerState.message
            else -> getString(R.string.peer_disconnected)
        }
    }

    private fun ensureVoicePermission() {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.POST_NOTIFICATIONS
        )
        val needsRequest = permissions.any { perm ->
            ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED
        }
        if (needsRequest) {
            permissionLauncher.launch(permissions)
        } else {
            VoiceRecognitionService.start(this)
        }
    }
}
