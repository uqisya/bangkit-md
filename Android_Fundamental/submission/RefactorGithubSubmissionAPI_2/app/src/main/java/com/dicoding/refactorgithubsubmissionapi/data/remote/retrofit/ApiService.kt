package com.dicoding.refactorgithubsubmissionapi.data.remote.retrofit

import com.dicoding.refactorgithubsubmissionapi.data.remote.response.DetailUserResponse
import com.dicoding.refactorgithubsubmissionapi.data.remote.response.ItemsItem
import com.dicoding.refactorgithubsubmissionapi.data.remote.response.QueryUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getGithubUserAccount(
        @Header("Authorization") token: String,
        @Query("q") username: String
    ): Call<QueryUserResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowersUser(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowingUser(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}