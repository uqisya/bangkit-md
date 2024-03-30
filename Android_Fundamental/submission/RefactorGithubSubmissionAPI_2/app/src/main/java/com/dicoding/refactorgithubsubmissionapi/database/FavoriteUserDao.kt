package com.dicoding.refactorgithubsubmissionapi.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteUserDao {
    /*
    * sengaja menggunakan raw query semua untuk sekalian belajar dan mengasah skill sql query saya
     */

    @Query("INSERT INTO favoriteuser (username, avatarUrl) VALUES (:username, :avatarUrl)")
    fun insertUser(username: String, avatarUrl: String)

    @Query("SELECT * FROM favoriteuser WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser>

    @Query("DELETE FROM favoriteuser WHERE username = :username")
    fun deleteUser(username: String)

    @Query("SELECT * FROM favoriteuser ORDER BY username ASC")
    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>>

}