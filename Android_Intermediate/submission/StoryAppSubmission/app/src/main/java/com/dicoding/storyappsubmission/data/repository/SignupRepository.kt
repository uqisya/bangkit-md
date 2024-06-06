package com.dicoding.storyappsubmission.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.dicoding.storyappsubmission.data.ResultState
import com.dicoding.storyappsubmission.data.model.UserModel
import com.dicoding.storyappsubmission.data.remote.response.ErrorResponse
import com.dicoding.storyappsubmission.data.remote.response.RegisterResponse
import com.dicoding.storyappsubmission.data.remote.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException

class SignupRepository(private val apiService: ApiService) {
    fun registerAccount(user: UserModel) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.registerAccount(user.name, user.email, user.password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorJsonString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(errorJsonString, ErrorResponse::class.java)
            emit(
                errorBody.message?.let {
                    ResultState.Error(it)
                }
            )
        }
    }

    companion object {
        @Volatile
        private var instance: SignupRepository? = null
        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: SignupRepository(apiService)
            }.also { instance = it }
    }
}