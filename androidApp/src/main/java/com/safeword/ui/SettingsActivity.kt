package com.safeword.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.safeword.R
import com.safeword.databinding.ActivitySettingsBinding
import com.safeword.ui.settings.SettingsViewModel
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
        binding.switchPlaySiren.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setPlaySiren(isChecked)
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
                    binding.switchPlaySiren.isChecked = settings.playSiren
                    binding.switchTestMode.isChecked = settings.testMode
                }
            }
        }
    }
}
