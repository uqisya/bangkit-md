package com.dicoding.storyappsubmission.data.remote.retrofit

import com.dicoding.storyappsubmission.data.remote.response.LoginResponse
import com.dicoding.storyappsubmission.data.remote.response.RegisterResponse
import com.dicoding.storyappsubmission.data.remote.response.StoryResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

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
    @GET("stories")
    suspend fun getAllStories(): StoryResponse
}