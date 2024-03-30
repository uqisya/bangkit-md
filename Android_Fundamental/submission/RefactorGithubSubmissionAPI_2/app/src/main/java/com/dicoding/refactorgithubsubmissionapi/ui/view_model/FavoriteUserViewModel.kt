package com.dicoding.refactorgithubsubmissionapi.ui.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.refactorgithubsubmissionapi.database.FavoriteUser
import com.dicoding.refactorgithubsubmissionapi.repository.FavoriteUserRepository

class FavoriteUserViewModel(application: Application) : ViewModel() {

    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> {
        return mFavoriteUserRepository.getAllFavoriteUsers()
    }
}