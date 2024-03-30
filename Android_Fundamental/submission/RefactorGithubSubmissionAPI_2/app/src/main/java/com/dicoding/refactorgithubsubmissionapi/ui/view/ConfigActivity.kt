package com.dicoding.refactorgithubsubmissionapi.ui.view

import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import com.dicoding.refactorgithubsubmissionapi.R
import com.dicoding.refactorgithubsubmissionapi.config.SettingPreferences
import com.dicoding.refactorgithubsubmissionapi.config.dataStore
import com.dicoding.refactorgithubsubmissionapi.databinding.ActivityConfigBinding
import com.dicoding.refactorgithubsubmissionapi.factory.ViewModelFactory
import com.dicoding.refactorgithubsubmissionapi.ui.view_model.ConfigViewModel
import com.google.android.material.switchmaterial.SwitchMaterial

class ConfigActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfigBinding
    private lateinit var switchTheme: SwitchMaterial
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        switchTheme = binding.switchTheme

        val pref = SettingPreferences.getInstance(application.dataStore)
        val configViewModel = ViewModelFactory.getViewModel(this@ConfigActivity, ConfigViewModel::class.java, pref)
        observeConfigTheme(configViewModel)

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            configViewModel.saveThemeSetting(isChecked)
        }
    }

    private fun observeConfigTheme(configViewModel: ConfigViewModel) {
        configViewModel.configTheme.observe(this@ConfigActivity) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }
    }
}