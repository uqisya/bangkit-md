package com.dicoding.storyappsubmission.data.remote.retrofit

import com.dicoding.storyappsubmission.data.remote.response.AddStoryResponse
import com.dicoding.storyappsubmission.data.remote.response.LoginResponse
import com.dicoding.storyappsubmission.data.remote.response.RegisterResponse
import com.dicoding.storyappsubmission.data.remote.response.StoryDetailResponse
import com.dicoding.storyappsubmission.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    // menggunakan suspend agar dapat dijalankan secara asynchronous
    // fungsi register akan menunda eksekusinya saat menunggu respon dari server
    @FormUrlEncoded
    @POST("register")
    suspend fun registerAccount(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun loginAccount(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse

    // karena untuk hit API GET stories ini perlu token, maka perlu tambahkan token
    // tapi dengan menambahkan @Header("Authorization") token: String di setiap endpoint akan merepotkan
    // maka buatlah di apiConfig.kt
    @GET("stories")  // auth
    suspend fun getAllStories(): StoryResponse

    @GET("stories/{id}")  // auth
    suspend fun getDetailStoryByID(
        @Path("id") storyID: String
    ) : StoryDetailResponse

    @Multipart
    @POST("stories") // auth
    suspend fun addNewStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ) : AddStoryResponse
}