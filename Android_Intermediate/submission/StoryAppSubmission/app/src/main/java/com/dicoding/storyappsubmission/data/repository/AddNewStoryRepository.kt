package com.dicoding.storyappsubmission.data.repository

import androidx.lifecycle.liveData
import com.dicoding.storyappsubmission.data.ResultState
import com.dicoding.storyappsubmission.data.model.StoryModel
import com.dicoding.storyappsubmission.data.remote.response.ErrorResponse
import com.dicoding.storyappsubmission.data.remote.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException

class AddNewStoryRepository(private val apiService: ApiService) {

    fun addNewStory(story: StoryModel) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.addNewStory(story.file, story.description)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorJsonString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(errorJsonString, ErrorResponse::class.java)
            emit(
                errorBody.message?.let {
                    ResultState.Error(it)
                }
            )
        }
    }

    companion object {
        @Volatile
        private var instance: AddNewStoryRepository? = null
        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: AddNewStoryRepository(apiService)
            }.also { instance = it }
    }
}