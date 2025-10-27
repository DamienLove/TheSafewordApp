package com.safeword.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.safeword.BuildConfig
import com.safeword.R
import com.safeword.databinding.ViewNativeAdBinding
import com.safeword.service.EmergencyHandlerService
import com.safeword.service.SafeWordPeerService
import com.safeword.service.VoiceRecognitionService
import com.safeword.shared.domain.model.AlertSource
import com.safeword.ui.main.MainDestination
import com.safeword.ui.main.MainScreen
import com.safeword.ui.main.MainViewModel
import com.safeword.ui.onboarding.OnboardingActivity
import com.safeword.ui.theme.SafeWordTheme
import com.safeword.util.OnboardingPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val snackbarHostState = SnackbarHostState()
    private var currentNativeAd by mutableStateOf<NativeAd?>(null)
    private var isAdVisible by mutableStateOf(false)
    private var lastListeningState: Boolean? = null

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val granted = result.values.all { it }
        if (granted) {
            viewModel.setListening(true)
        } else {
            viewModel.setListening(false)
            lifecycleScope.launch {
                snackbarHostState.showSnackbar(getString(R.string.enable_permissions))
            }
        }
    }

    private val requiredPermissions = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.POST_NOTIFICATIONS
    )

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!OnboardingPreferences.isCompleted(this)) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
            return
        }

        val activity = this
        setContent {
            SafeWordTheme {
                val windowSizeClass = calculateWindowSizeClass(activity)
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                MainScreen(
                    windowSizeClass = windowSizeClass,
                    state = uiState,
                    snackbarHostState = snackbarHostState,
                    nativeAd = currentNativeAd,
                    adVisible = isAdVisible && BuildConfig.FEATURE_ADS_ENABLED,
                    adsEnabled = BuildConfig.FEATURE_ADS_ENABLED,
                    onCloseAd = { clearNativeAd() },
                    onToggleListening = { toggleListeningState(uiState.listeningEnabled) },
                    onNavigate = { destination -> handleNavigation(destination) },
                    onBindNativeAdView = { ad, binding -> populateNativeAdView(ad, binding) }
                )
            }
        }

        SafeWordPeerService.start(this)
        loadNativeAdIfNeeded()
        observeListeningState()
        handleShortcutIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleShortcutIntent(intent)
    }

    override fun onDestroy() {
        clearNativeAd()
        super.onDestroy()
    }

    private fun handleNavigation(destination: MainDestination): Boolean {
        return when (destination) {
            MainDestination.Dashboard -> true
            MainDestination.Contacts -> {
                startActivity(Intent(this, ContactsActivity::class.java))
                false
            }
            MainDestination.SafeWords -> {
                startActivity(Intent(this, SafeWordWizardActivity::class.java))
                false
            }
            MainDestination.Settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                false
            }
        }
    }

    private fun toggleListeningState(isCurrentlyListening: Boolean) {
        if (isCurrentlyListening) {
            viewModel.setListening(false)
            return
        }

        val hasPermissions = requiredPermissions.all { perm ->
            ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED
        }
        if (hasPermissions) {
            viewModel.setListening(true)
        } else {
            permissionLauncher.launch(requiredPermissions)
        }
    }

    private fun loadNativeAdIfNeeded() {
        if (!BuildConfig.FEATURE_ADS_ENABLED) {
            clearNativeAd()
            return
        }

        val adLoader = AdLoader.Builder(this, getString(R.string.admob_native_unit))
            .forNativeAd { nativeAd ->
                currentNativeAd?.destroy()
                currentNativeAd = nativeAd
                isAdVisible = true
            }
            .withAdListener(object : com.google.android.gms.ads.AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    clearNativeAd()
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

    private fun clearNativeAd() {
        currentNativeAd?.destroy()
        currentNativeAd = null
        isAdVisible = false
    }

    private fun handleShortcutIntent(intent: Intent?) {
        when (intent?.action) {
            MainActivity.ACTION_START_LISTENING -> {
                val isListening = viewModel.uiState.value.listeningEnabled
                if (!isListening) {
                    toggleListeningState(isListening)
                }
            }
            MainActivity.ACTION_RUN_TEST -> {
                EmergencyHandlerService.trigger(this, "TEST", AlertSource.TEST)
            }
        }
    }

    private fun populateNativeAdView(nativeAd: NativeAd, binding: ViewNativeAdBinding) {
        val adView = binding.root as NativeAdView
        adView.headlineView = binding.adHeadline
        adView.bodyView = binding.adBody
        adView.iconView = binding.adIcon
        adView.callToActionView = binding.adCallToAction
        adView.mediaView = binding.adMedia
        adView.advertiserView = binding.adAdvertiser

        binding.adHeadline.text = nativeAd.headline

        val body = nativeAd.body
        if (body.isNullOrBlank()) {
            binding.adBody.visibility = android.view.View.GONE
        } else {
            binding.adBody.visibility = android.view.View.VISIBLE
            binding.adBody.text = body
        }

        val advertiser = nativeAd.advertiser
        if (advertiser.isNullOrBlank()) {
            binding.adAdvertiser.visibility = android.view.View.GONE
        } else {
            binding.adAdvertiser.visibility = android.view.View.VISIBLE
            binding.adAdvertiser.text = advertiser
        }

        val icon = nativeAd.icon
        if (icon != null) {
            binding.adIcon.visibility = android.view.View.VISIBLE
            binding.adIcon.setImageDrawable(icon.drawable)
        } else {
            binding.adIcon.visibility = android.view.View.GONE
        }

        val ctaText = nativeAd.callToAction ?: getString(R.string.learn_more)
        binding.adCallToAction.text = ctaText
        binding.adCallToAction.visibility =
            if (nativeAd.callToAction.isNullOrBlank()) android.view.View.GONE else android.view.View.VISIBLE

        binding.adMedia.setMediaContent(nativeAd.mediaContent)

        adView.setNativeAd(nativeAd)
    }

    private fun observeListeningState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    val listening = state.listeningEnabled
                    if (lastListeningState == listening) return@collectLatest
                    lastListeningState = listening
                    if (listening) {
                        VoiceRecognitionService.start(this@MainActivity)
                    } else {
                        VoiceRecognitionService.stop(this@MainActivity)
                    }
                }
            }
        }
    }

    companion object {
        const val ACTION_START_LISTENING = "com.safeword.action.START_LISTENING"
        const val ACTION_RUN_TEST = "com.safeword.action.RUN_TEST"
    }
}
