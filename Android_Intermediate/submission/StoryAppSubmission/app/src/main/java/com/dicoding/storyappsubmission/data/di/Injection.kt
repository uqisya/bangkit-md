package com.dicoding.storyappsubmission.data.di

import com.dicoding.storyappsubmission.data.remote.retrofit.ApiConfig
import com.dicoding.storyappsubmission.data.remote.retrofit.ApiService
import com.dicoding.storyappsubmission.data.repository.LoginRepository
import com.dicoding.storyappsubmission.data.repository.SignupRepository

object Injection {
    private fun provideApiService(): ApiService {
        return ApiConfig.getApiService()
    }

    fun provideSignupRepository(): SignupRepository {
        val apiService = provideApiService()
        return SignupRepository.getInstance(apiService)
    }

    fun provideLoginRepository(): LoginRepository {
        val apiService = provideApiService()
        return LoginRepository.getInstance(apiService)
    }

}