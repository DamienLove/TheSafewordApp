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

        binding.switchMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setModeIncoming(isChecked)
            binding.switchMode.text = if (isChecked) getString(R.string.incoming_mode) else getString(R.string.outgoing_mode)
        }

        binding.buttonSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.buttonContacts.setOnClickListener {
            startActivity(Intent(this, ContactsActivity::class.java))
        }

        binding.buttonSafeWords.setOnClickListener {
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
        binding.switchMode.isChecked = state.incomingMode
        binding.switchMode.text = if (state.incomingMode) getString(R.string.incoming_mode) else getString(R.string.outgoing_mode)
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
