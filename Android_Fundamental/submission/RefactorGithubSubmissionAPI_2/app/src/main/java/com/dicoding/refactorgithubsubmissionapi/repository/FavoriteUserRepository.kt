package com.dicoding.refactorgithubsubmissionapi.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.refactorgithubsubmissionapi.database.FavoriteUser
import com.dicoding.refactorgithubsubmissionapi.database.FavoriteUserDao
import com.dicoding.refactorgithubsubmissionapi.database.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val dbInstance = FavoriteUserRoomDatabase.getDatabaseInstance(application)
        mFavoriteUserDao = dbInstance.favoriteUserDao()
    }

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> {
        return mFavoriteUserDao.getAllFavoriteUsers()
    }

    fun insertUser(user: FavoriteUser) {
        executorService.execute {
            mFavoriteUserDao.insertUser(user.username.toString(), user.avatarUrl.toString())
        }
    }

    fun getFavoriteUserByUsername(user: FavoriteUser): LiveData<FavoriteUser> {
            return mFavoriteUserDao.getFavoriteUserByUsername(user.username)
    }

    fun deleteUser(user: FavoriteUser) {
        executorService.execute {
            mFavoriteUserDao.deleteUser(user.username)
        }
    }
}