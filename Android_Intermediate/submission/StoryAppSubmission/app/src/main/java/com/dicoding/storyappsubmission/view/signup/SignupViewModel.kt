package com.dicoding.storyappsubmission.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyappsubmission.data.ResultState
import com.dicoding.storyappsubmission.data.model.UserModel
import com.dicoding.storyappsubmission.data.remote.response.ErrorResponse
import com.dicoding.storyappsubmission.data.remote.response.RegisterResponse
import com.dicoding.storyappsubmission.data.repository.SignupRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignupViewModel(private val repository: SignupRepository) : ViewModel() {

    fun registerAccount(user: UserModel) = repository.registerAccount(user)
}