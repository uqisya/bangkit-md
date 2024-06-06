package com.dicoding.storyappsubmission.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storyappsubmission.data.repository.LoginRepository
import com.dicoding.storyappsubmission.utils.UserPreferences
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRepository, private val userPref: UserPreferences) : ViewModel() {

    fun getAuthToken() : LiveData<String?> {
        return userPref.getAuthToken().asLiveData()
    }

    fun saveAuthToken(token: String) {
        viewModelScope.launch {
            userPref.saveAuthToken(token)
        }
    }

    fun loginAccount(email: String, password: String) = repository.loginAccount(email, password)
}