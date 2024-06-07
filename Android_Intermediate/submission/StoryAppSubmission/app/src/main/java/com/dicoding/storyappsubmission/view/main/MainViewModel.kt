package com.dicoding.storyappsubmission.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storyappsubmission.data.repository.StoryRepository
import com.dicoding.storyappsubmission.utils.UserPreferences
import kotlinx.coroutines.launch

class MainViewModel(private val repository: StoryRepository, private val userPref: UserPreferences) : ViewModel() {

    fun getAllStories() = repository.getAllStories()

    fun getAuthToken() : LiveData<String?> {
        return userPref.getAuthToken().asLiveData()
    }

    fun logoutAccount() {
        removeUserAuthToken()
    }

    private fun removeUserAuthToken() {
        viewModelScope.launch {
            userPref.removeAuthToken()
        }
    }
}