package com.safeword.ui.onboarding

import android.Manifest
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.safeword.R
import com.safeword.databinding.ActivityOnboardingBinding
import com.safeword.ui.MainActivity
import com.safeword.util.OnboardingPreferences

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    private val corePermissions = arrayOf(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.SEND_SMS,
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        updatePermissionStates()
    }

    private val microphonePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        updatePermissionStates()
    }

    private val corePermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        updatePermissionStates()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            completeAndFinish()
        }

        binding.buttonSkip.setOnClickListener { completeAndFinish() }
        binding.buttonFinish.setOnClickListener { completeAndFinish() }

        binding.buttonAllowNotifications.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !areNotificationsEnabled()) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                openNotificationSettings()
            }
        }

        binding.buttonAllowMicrophone.setOnClickListener {
            if (isPermissionGranted(Manifest.permission.RECORD_AUDIO)) {
                openAppSettings()
            } else {
                microphonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }

        binding.buttonCorePermissions.setOnClickListener {
            val pendingPermissions = corePermissions.filterNot { isPermissionGranted(it) }
            if (pendingPermissions.isEmpty()) {
                openAppSettings()
            } else {
                corePermissionsLauncher.launch(pendingPermissions.toTypedArray())
            }
        }

        binding.buttonBatterySettings.setOnClickListener {
            startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
        }

        binding.buttonDndSettings.setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
        }

        updatePermissionStates()
    }

    override fun onResume() {
        super.onResume()
        updatePermissionStates()
    }

    private fun updatePermissionStates() {
        val notificationsGranted = areNotificationsEnabled()
        val microphoneGranted = isPermissionGranted(Manifest.permission.RECORD_AUDIO)
        val coreGranted = corePermissions.all { isPermissionGranted(it) }
        val batteryGranted = isBatteryOptimizationDisabled()
        val dndGranted = isDoNotDisturbAccessGranted()

        binding.textNotificationsStatus.text = statusLabel(notificationsGranted)
        binding.buttonAllowNotifications.text = if (notificationsGranted) {
            getString(R.string.onboarding_manage_notifications)
        } else {
            getString(R.string.onboarding_allow_notifications)
        }

        binding.textMicrophoneStatus.text = statusLabel(microphoneGranted)
        binding.textCoreStatus.text = statusLabel(coreGranted)
        binding.textBatteryStatus.text = statusLabel(batteryGranted)
        binding.textDndStatus.text = statusLabel(dndGranted)

        binding.buttonFinish.isEnabled = notificationsGranted && microphoneGranted
    }

    private fun statusLabel(granted: Boolean): String =
        if (granted) getString(R.string.status_granted) else getString(R.string.status_pending)

    private fun areNotificationsEnabled(): Boolean =
        NotificationManagerCompat.from(this).areNotificationsEnabled()

    private fun isPermissionGranted(permission: String): Boolean =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    private fun isBatteryOptimizationDisabled(): Boolean {
        val powerManager = getSystemService(PowerManager::class.java)
        return powerManager?.isIgnoringBatteryOptimizations(packageName) == true
    }

    private fun isDoNotDisturbAccessGranted(): Boolean {
        val notificationManager = getSystemService(NotificationManager::class.java)
        return notificationManager?.isNotificationPolicyAccessGranted == true
    }

    private fun openNotificationSettings() {
        startActivity(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        })
    }

    private fun openAppSettings() {
        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = android.net.Uri.parse("package:$packageName")
        })
    }

    private fun completeAndFinish() {
        OnboardingPreferences.setCompleted(this)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

