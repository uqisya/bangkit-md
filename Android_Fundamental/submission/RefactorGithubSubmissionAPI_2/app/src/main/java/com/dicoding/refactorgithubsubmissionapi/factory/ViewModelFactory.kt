package com.dicoding.refactorgithubsubmissionapi.factory

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.refactorgithubsubmissionapi.ui.view_model.DetailUserViewModel
import com.dicoding.refactorgithubsubmissionapi.ui.view_model.FavoriteUserViewModel
import com.dicoding.refactorgithubsubmissionapi.ui.view_model.MainViewModel
import com.dicoding.refactorgithubsubmissionapi.ui.view_model.UserFollowerFollowingViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application)
                }
            }
            return INSTANCE as ViewModelFactory
        }

        fun <T: ViewModel> getViewModel(activity: FragmentActivity, modelClass: Class<T>): T {
            val factory = ViewModelFactory.getInstance(activity.application)
            return ViewModelProvider(activity, factory)[modelClass]
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel() as T
        } else if (modelClass.isAssignableFrom(UserFollowerFollowingViewModel::class.java)) {
            return UserFollowerFollowingViewModel() as T
        } else if (modelClass.isAssignableFrom(DetailUserViewModel::class.java)) {
            return DetailUserViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(FavoriteUserViewModel::class.java)) {
            return FavoriteUserViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}