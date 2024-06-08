package com.dicoding.storyappsubmission.view.signup

import androidx.lifecycle.ViewModel
import com.dicoding.storyappsubmission.data.model.UserModel
import com.dicoding.storyappsubmission.data.repository.SignupRepository

class SignupViewModel(private val repository: SignupRepository) : ViewModel() {

    fun registerAccount(user: UserModel) = repository.registerAccount(user)
}