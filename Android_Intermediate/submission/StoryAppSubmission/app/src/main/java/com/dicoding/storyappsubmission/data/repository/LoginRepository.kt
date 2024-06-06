package com.dicoding.storyappsubmission.data.repository

import androidx.lifecycle.liveData
import com.dicoding.storyappsubmission.data.ResultState
import com.dicoding.storyappsubmission.data.model.UserModel
import com.dicoding.storyappsubmission.data.remote.response.ErrorResponse
import com.dicoding.storyappsubmission.data.remote.response.LoginResponse
import com.dicoding.storyappsubmission.data.remote.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException

class LoginRepository(private val apiService: ApiService) {
    fun loginAccount(email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.loginAccount(email, password)
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
        private var instance: LoginRepository? = null
        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: LoginRepository(apiService)
            }.also { instance = it }
    }
}