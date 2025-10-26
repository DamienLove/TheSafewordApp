package com.safeword.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.safeword.databinding.ActivityOnboardingBinding
import com.safeword.ui.MainActivity
import com.safeword.util.OnboardingPreferences

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

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
            startActivity(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            })
        }
        binding.buttonAllowMicrophone.setOnClickListener {
            startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = android.net.Uri.parse("package:$packageName")
            })
        }
        binding.buttonBatterySettings.setOnClickListener {
            startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
        }
        binding.buttonDndSettings.setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
        }
    }

    private fun completeAndFinish() {
        OnboardingPreferences.setCompleted(this)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
