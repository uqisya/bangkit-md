package com.dicoding.refactorgithubsubmissionapi.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.refactorgithubsubmissionapi.config.SettingPreferences
import kotlinx.coroutines.launch

class ConfigViewModel(private val pref: SettingPreferences) : ViewModel() {

    val configTheme: LiveData<Boolean>

    init {
        configTheme = getThemeSettings()
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}