package com.dicoding.storyappsubmission.data.di

import android.content.Context
import com.dicoding.storyappsubmission.data.remote.retrofit.ApiConfig
import com.dicoding.storyappsubmission.data.remote.retrofit.ApiService
import com.dicoding.storyappsubmission.data.repository.AddNewStoryRepository
import com.dicoding.storyappsubmission.data.repository.ListStoryRepository
import com.dicoding.storyappsubmission.data.repository.LoginRepository
import com.dicoding.storyappsubmission.data.repository.SignupRepository
import com.dicoding.storyappsubmission.data.repository.StoryDetailRepository
import com.dicoding.storyappsubmission.utils.UserPreferences
import com.dicoding.storyappsubmission.utils.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    private fun provideApiService(): ApiService {
        return ApiConfig.getApiService()
    }

    private fun provideApiServiceToken(authToken: String): ApiService {
        return ApiConfig.getApiService(authToken)
    }

    fun provideSignupRepository(): SignupRepository {
        val apiService = provideApiService()
        return SignupRepository.getInstance(apiService)
    }

    fun provideLoginRepository(): LoginRepository {
        val apiService = provideApiService()
        return LoginRepository.getInstance(apiService)
    }

    private fun getAuthToken(context: Context): String? {
        val userPref = UserPreferences.getInstance(context.dataStore)
        val userToken = runBlocking {
            userPref.getAuthToken().first()
        }
        return userToken
    }

    fun provideListStoryRepository(context: Context): ListStoryRepository {
        val userToken = getAuthToken(context)

        val apiService = userToken?.let { authToken ->
            provideApiServiceToken(authToken)
        }
        if (apiService != null) return ListStoryRepository.getInstance(apiService)
        else throw IllegalStateException("User auth token is null")
    }

    fun provideStoryDetailRepository(context: Context): StoryDetailRepository {
        val userToken = getAuthToken(context)

        val apiService = userToken?.let { authToken ->
            provideApiServiceToken(authToken)
        }

        if (apiService != null) return StoryDetailRepository.getInstance(apiService)
        else throw IllegalStateException("User auth token is null")
    }

    fun provideAddNewStoryRepository(context: Context): AddNewStoryRepository {
        val userToken = getAuthToken(context)
        println(userToken)

        val apiService = userToken?.let { authToken ->
            provideApiServiceToken(authToken)
        }
        if (apiService != null) return AddNewStoryRepository.getInstance(apiService)
        else throw IllegalStateException("User auth token is null")
    }

}