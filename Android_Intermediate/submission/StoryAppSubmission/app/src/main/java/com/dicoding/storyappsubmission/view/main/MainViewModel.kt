package com.dicoding.storyappsubmission.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyappsubmission.data.remote.response.ListStoryItem
import com.dicoding.storyappsubmission.data.repository.ListStoryRepository
import com.dicoding.storyappsubmission.utils.UserPreferences
import kotlinx.coroutines.launch

class MainViewModel(
    private val listStoryRepository: ListStoryRepository,
    private val userPref: UserPreferences
) : ViewModel() {

    fun getAllStories() = listStoryRepository.getAllStories().cachedIn(viewModelScope)

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