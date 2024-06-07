package com.dicoding.storyappsubmission.data.repository

import com.dicoding.storyappsubmission.data.remote.retrofit.ApiService
import com.dicoding.storyappsubmission.utils.UserPreferences

class StoryRepository(private val apiService: ApiService) {


    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(apiService: ApiService, userPref: UserPreferences) =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService)
            }.also { instance = it }
    }
}