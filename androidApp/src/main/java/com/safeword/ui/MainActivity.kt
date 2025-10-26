package com.safeword.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.gms.ads.VideoOptions
import com.google.android.material.snackbar.Snackbar
import com.safeword.BuildConfig
import com.safeword.R
import com.safeword.databinding.ActivityMainBinding
import com.safeword.databinding.ViewNativeAdBinding
import com.safeword.service.EmergencyHandlerService
import com.safeword.service.SafeWordPeerService
import com.safeword.service.VoiceRecognitionService
import com.safeword.shared.domain.model.AlertSource
import com.safeword.ui.SafeWordWizardActivity
import com.safeword.ui.main.MainUiState
import com.safeword.ui.main.MainViewModel
import com.safeword.ui.onboarding.OnboardingActivity
import com.safeword.util.OnboardingPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private var updatingModeSelection = false
    private var currentNativeAd: NativeAd? = null
    private var isListening = false

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val granted = result.values.all { it }
        if (granted) {
            viewModel.setListening(true)
        } else {
            viewModel.setListening(false)
            Snackbar.make(binding.root, R.string.enable_permissions, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!OnboardingPreferences.isCompleted(this)) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
            return
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureFeatureAccess()
        loadNativeAdIfNeeded()

        SafeWordPeerService.start(this)

        binding.cardListening.setOnClickListener {
            toggleListeningState()
        }

        binding.modeToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked || updatingModeSelection) return@addOnButtonCheckedListener
            val incoming = checkedId == binding.buttonModeIncoming.id
            viewModel.setModeIncoming(incoming)
            updateModeButtons(incoming)
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

        updateModeButtons(binding.modeToggleGroup.checkedButtonId == binding.buttonModeIncoming.id)
        observeState()
        handleShortcutIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleShortcutIntent(intent)
    }

    override fun onDestroy() {
        currentNativeAd?.destroy()
        currentNativeAd = null
        super.onDestroy()
    }

    private fun configureFeatureAccess() {
        if (!BuildConfig.FEATURE_INCOMING_SMS) {
            binding.buttonModeIncoming.isEnabled = false
            binding.buttonModeIncoming.text = getString(R.string.mode_incoming_locked)
            binding.buttonModeIncoming.alpha = 0.5f
        } else {
            binding.buttonModeIncoming.isEnabled = true
            binding.buttonModeIncoming.alpha = 1f
        }
    }

    private fun loadNativeAdIfNeeded() {
        if (!BuildConfig.FEATURE_ADS_ENABLED) {
            binding.nativeAdContainer.visibility = View.GONE
            return
        }

        val adLoader = AdLoader.Builder(this, getString(R.string.admob_native_unit))
            .forNativeAd { nativeAd ->
                currentNativeAd?.destroy()
                currentNativeAd = nativeAd
                val adBinding = ViewNativeAdBinding.inflate(LayoutInflater.from(this))
                populateNativeAdView(nativeAd, adBinding)
                binding.nativeAdContainer.removeAllViews()
                binding.nativeAdContainer.addView(adBinding.root)
                binding.nativeAdContainer.visibility = View.VISIBLE
            }
            .withAdListener(object : com.google.android.gms.ads.AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    binding.nativeAdContainer.visibility = View.GONE
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setVideoOptions(
                        VideoOptions.Builder()
                            .setStartMuted(true)
                            .build()
                    )
                    .build()
            )
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adBinding: ViewNativeAdBinding) {
        val adView = adBinding.root
        adView.headlineView = adBinding.adHeadline
        adView.bodyView = adBinding.adBody
        adView.iconView = adBinding.adIcon
        adView.callToActionView = adBinding.adCallToAction
        adView.mediaView = adBinding.adMedia
        adView.advertiserView = adBinding.adAdvertiser

        adBinding.adHeadline.text = nativeAd.headline

        val body = nativeAd.body
        if (body.isNullOrBlank()) {
            adBinding.adBody.visibility = View.GONE
        } else {
            adBinding.adBody.visibility = View.VISIBLE
            adBinding.adBody.text = body
        }

        val advertiser = nativeAd.advertiser
        if (advertiser.isNullOrBlank()) {
            adBinding.adAdvertiser.visibility = View.GONE
        } else {
            adBinding.adAdvertiser.visibility = View.VISIBLE
            adBinding.adAdvertiser.text = advertiser
        }

        val icon = nativeAd.icon
        if (icon != null) {
            adBinding.adIcon.visibility = View.VISIBLE
            adBinding.adIcon.setImageDrawable(icon.drawable)
        } else {
            adBinding.adIcon.visibility = View.GONE
        }

        val ctaText = nativeAd.callToAction ?: getString(R.string.learn_more)
        adBinding.adCallToAction.text = ctaText
        adBinding.adCallToAction.visibility =
            if (nativeAd.callToAction.isNullOrBlank()) View.GONE else View.VISIBLE

        adBinding.adMedia.setMediaContent(nativeAd.mediaContent)

        adView.setNativeAd(nativeAd)
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { renderState(it) }
            }
        }
    }

    private fun renderState(state: MainUiState) {
        val changed = isListening != state.listeningEnabled
        isListening = state.listeningEnabled
        updateListeningUi(isListening)
        if (changed) {
            if (isListening) {
                VoiceRecognitionService.start(this)
            } else {
                VoiceRecognitionService.stop(this)
            }
        }
        updatingModeSelection = true
        if (BuildConfig.FEATURE_INCOMING_SMS && state.incomingMode) {
            binding.modeToggleGroup.check(binding.buttonModeIncoming.id)
        } else {
            if (!BuildConfig.FEATURE_INCOMING_SMS && state.incomingMode) {
                viewModel.setModeIncoming(false)
            }
            binding.modeToggleGroup.check(binding.buttonModeOutgoing.id)
        }
        updatingModeSelection = false
        updateModeButtons(state.incomingMode && BuildConfig.FEATURE_INCOMING_SMS)
        binding.textContacts.text = resources.getQuantityString(R.plurals.contacts_count, state.contacts, state.contacts)
        binding.textPeerState.text = when (state.peerState) {
            is com.safeword.shared.bridge.model.PeerBridgeState.Connected -> getString(R.string.peer_connected)
            is com.safeword.shared.bridge.model.PeerBridgeState.Error -> state.peerState.message
            else -> getString(R.string.peer_disconnected)
        }
    }

    private fun updateModeButtons(incomingSelected: Boolean) {
        val selectedBackground = ContextCompat.getColor(this, R.color.mode_selected_bg)
        val unselectedBackground = ContextCompat.getColor(this, R.color.mode_unselected_bg)
        val selectedText = ContextCompat.getColor(this, R.color.mode_selected_text)
        val unselectedText = ContextCompat.getColor(this, R.color.mode_unselected_text)

        val incomingColor = if (incomingSelected) selectedBackground else unselectedBackground
        val outgoingColor = if (incomingSelected) unselectedBackground else selectedBackground
        val incomingTextColor = if (incomingSelected) selectedText else unselectedText
        val outgoingTextColor = if (incomingSelected) unselectedText else selectedText

        binding.buttonModeIncoming.backgroundTintList = ColorStateList.valueOf(incomingColor)
        binding.buttonModeOutgoing.backgroundTintList = ColorStateList.valueOf(outgoingColor)
        binding.buttonModeIncoming.setTextColor(incomingTextColor)
        binding.buttonModeOutgoing.setTextColor(outgoingTextColor)
    }

    private fun toggleListeningState() {
        if (isListening) {
            viewModel.setListening(false)
            return
        }

        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.POST_NOTIFICATIONS
        )
        val hasPermissions = permissions.all { perm ->
            ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED
        }
        if (hasPermissions) {
            viewModel.setListening(true)
        } else {
            permissionLauncher.launch(permissions)
        }
    }

    private fun updateListeningUi(listening: Boolean) {
        binding.imageListeningGlow.isVisible = listening
        binding.cardListening.setBackgroundResource(
            if (listening) R.drawable.bg_listening_button_on else R.drawable.bg_listening_button_off
        )
        binding.imageListeningIcon.setColorFilter(
            ContextCompat.getColor(
                this,
                if (listening) R.color.safeword_on_primary else R.color.accent_teal
            )
        )
        binding.textListeningStatus.text = if (listening) {
            getString(R.string.main_listening_active)
        } else {
            getString(R.string.main_listening_inactive)
        }
        binding.textStatusChip.text = if (listening) {
            getString(R.string.main_status_listening_chip_on)
        } else {
            getString(R.string.main_status_listening_chip_off)
        }
    }

    private fun handleShortcutIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_START_LISTENING -> {
                if (!isListening) {
                    toggleListeningState()
                }
            }
            ACTION_RUN_TEST -> {
                EmergencyHandlerService.trigger(this, "TEST", AlertSource.TEST)
            }
        }
    }

    companion object {
        const val ACTION_START_LISTENING = "com.safeword.action.START_LISTENING"
        const val ACTION_RUN_TEST = "com.safeword.action.RUN_TEST"
    }
}
