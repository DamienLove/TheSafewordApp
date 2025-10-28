package com.safeword.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.safeword.R
import com.safeword.databinding.ActivitySettingsBinding
import com.safeword.ui.settings.SettingsViewModel
import com.safeword.shared.domain.model.AlertSound
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.sliderSensitivity.addOnChangeListener { _, value, fromUser ->
            binding.textSensitivityValue.text =
                getString(R.string.text_sensitivity_format, value.toInt())
            if (fromUser) viewModel.setSensitivity(value.toInt())
        }
        binding.switchIncludeLocation.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setIncludeLocation(isChecked)
        }
        val soundOptions = listOf(AlertSound.SIREN, AlertSound.GENTLE, AlertSound.SILENT)
        val soundLabels = soundOptions.map { soundLabel(it) }
        val soundAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, soundLabels)
        binding.inputEmergencySound.setAdapter(soundAdapter)
        binding.inputCheckinSound.setAdapter(soundAdapter)
        binding.inputEmergencySound.setOnItemClickListener { _, _, position, _ ->
            viewModel.setEmergencyAlertSound(soundOptions[position])
        }
        binding.inputCheckinSound.setOnItemClickListener { _, _, position, _ ->
            viewModel.setCheckInAlertSound(soundOptions[position])
        }
        binding.switchEmergencyOverride.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setEmergencyAlertBoost(isChecked)
        }
        binding.switchCheckinOverride.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setCheckInAlertBoost(isChecked)
        }
        binding.switchTestMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setTestMode(isChecked)
        }

        binding.buttonSave.setOnClickListener {
            viewModel.saveSafeWords(
                binding.inputSafeWordOne.text?.toString().orEmpty(),
                binding.inputSafeWordTwo.text?.toString().orEmpty()
            )
        }

        binding.buttonRunTest.setOnClickListener { viewModel.runTest() }

        observeSettings()
    }

    private fun observeSettings() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.settings.collect { settings ->
                    binding.inputSafeWordOne.setText(settings.safeWordOne)
                    binding.inputSafeWordTwo.setText(settings.safeWordTwo)
                    binding.sliderSensitivity.value = settings.sensitivity.toFloat()
                    binding.textSensitivityValue.text = getString(
                        R.string.text_sensitivity_format,
                        settings.sensitivity
                    )
                    binding.switchIncludeLocation.isChecked = settings.includeLocation
                    binding.switchEmergencyOverride.setOnCheckedChangeListener(null)
                    binding.inputEmergencySound.setText(soundLabel(settings.emergencyAlert.sound), false)
                    binding.switchEmergencyOverride.isChecked = settings.emergencyAlert.boostRinger
                    binding.switchEmergencyOverride.setOnCheckedChangeListener { _, isChecked ->
                        viewModel.setEmergencyAlertBoost(isChecked)
                    }
                    binding.switchCheckinOverride.setOnCheckedChangeListener(null)
                    binding.inputCheckinSound.setText(soundLabel(settings.nonEmergencyAlert.sound), false)
                    binding.switchCheckinOverride.isChecked = settings.nonEmergencyAlert.boostRinger
                    binding.switchCheckinOverride.setOnCheckedChangeListener { _, isChecked ->
                        viewModel.setCheckInAlertBoost(isChecked)
                    }
                    binding.switchTestMode.isChecked = settings.testMode
                }
            }
        }
    }

    private fun soundLabel(sound: AlertSound): String = when (sound) {
        AlertSound.SIREN -> getString(R.string.alert_sound_siren)
        AlertSound.GENTLE -> getString(R.string.alert_sound_gentle)
        AlertSound.SILENT -> getString(R.string.alert_sound_silent)
    }
}
