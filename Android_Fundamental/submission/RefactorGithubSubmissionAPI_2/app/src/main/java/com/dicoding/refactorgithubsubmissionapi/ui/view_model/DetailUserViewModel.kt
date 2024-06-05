package com.dicoding.refactorgithubsubmissionapi.ui.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.refactorgithubsubmissionapi.database.FavoriteUser
import com.dicoding.refactorgithubsubmissionapi.repository.FavoriteUserRepository

class DetailUserViewModel(application: Application) : ViewModel() {

    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun addUser(user: FavoriteUser) {
        mFavoriteUserRepository.insertUser(user)
    }

    fun getFavoriteUserByUsername(user: FavoriteUser): LiveData<FavoriteUser> {
        return mFavoriteUserRepository.getFavoriteUserByUsername(user)
    }

    fun deleteUser(user: FavoriteUser) {
        mFavoriteUserRepository.deleteUser(user)
    }
}