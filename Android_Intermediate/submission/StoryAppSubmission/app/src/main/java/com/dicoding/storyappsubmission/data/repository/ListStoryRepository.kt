package com.dicoding.storyappsubmission.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyappsubmission.data.ResultState
import com.dicoding.storyappsubmission.data.database.StoryDatabase
import com.dicoding.storyappsubmission.data.pager.StoryPagingSource
import com.dicoding.storyappsubmission.data.remote.response.ErrorResponse
import com.dicoding.storyappsubmission.data.remote.response.ListStoryItem
import com.dicoding.storyappsubmission.data.remote.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException

class ListStoryRepository(
    private val apiService: ApiService,
    private val database: StoryDatabase
) {

    fun getAllStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: ListStoryRepository? = null
        fun getInstance(apiService: ApiService, storyDatabase: StoryDatabase) =
            instance ?: synchronized(this) {
                instance ?: ListStoryRepository(apiService, storyDatabase)
            }.also { instance = it }
    }
}