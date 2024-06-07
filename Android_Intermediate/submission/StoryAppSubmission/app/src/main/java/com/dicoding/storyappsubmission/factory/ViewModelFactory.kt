package com.dicoding.storyappsubmission.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyappsubmission.data.di.Injection
import com.dicoding.storyappsubmission.utils.UserPreferences
import com.dicoding.storyappsubmission.utils.dataStore
import com.dicoding.storyappsubmission.view.login.LoginViewModel
import com.dicoding.storyappsubmission.view.main.MainViewModel
import com.dicoding.storyappsubmission.view.signup.SignupViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(Injection.provideSignupRepository()) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                val userPreferences = UserPreferences.getInstance(context.dataStore)
                LoginViewModel(Injection.provideLoginRepository(), userPreferences) as T
            }

            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                val userPreferences = UserPreferences.getInstance(context.dataStore)
                MainViewModel(Injection.provideStoryRepository(context), userPreferences) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(context.applicationContext)
                    .also { instance = it }
            }
    }
}