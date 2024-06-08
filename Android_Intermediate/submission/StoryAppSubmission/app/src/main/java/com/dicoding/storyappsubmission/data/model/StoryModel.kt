package com.dicoding.storyappsubmission.data.model

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class StoryModel(
    val file: MultipartBody.Part,
    val description: RequestBody,
    val lat: RequestBody?,
    val lon: RequestBody?
)