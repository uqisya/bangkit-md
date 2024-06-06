package com.dicoding.storyappsubmission.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences(context: Context) {
    private val dataStore: DataStore<Preferences> = context.dataStore

    private val KEY_AUTH_TOKEN = stringPreferencesKey("key_auth_token")

    fun getAuthToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[KEY_AUTH_TOKEN]
        }
    }

    suspend fun saveAuthToken(authToken: String) {
        dataStore.edit { preferences ->
            preferences[KEY_AUTH_TOKEN] = authToken
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(context: Context): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(context)
                INSTANCE = instance
                instance
            }
        }
    }
}