package com.dicoding.refactorgithubsubmissionapi.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.refactorgithubsubmissionapi.BuildConfig
import com.dicoding.refactorgithubsubmissionapi.data.remote.response.ItemsItem
import com.dicoding.refactorgithubsubmissionapi.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFollowerFollowingViewModel : ViewModel() {
    companion object {
        private const val TOKEN = BuildConfig.API_KEY
        private const val ERROR_PLACE = "UserFollowerFollowingViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userFollowersList = MutableLiveData<List<ItemsItem?>?>()
    val userFollowersList: LiveData<List<ItemsItem?>?> = _userFollowersList

    private val _userFollowingList = MutableLiveData<List<ItemsItem?>?>()
    val userFollowingList: LiveData<List<ItemsItem?>?> = _userFollowingList

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg

    internal fun getFollowersFromTheUser(usernameQuery: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getFollowersUser("token $TOKEN", username = usernameQuery)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val detailUser = response.body()
//                    whether null or empty, assign it
                        _userFollowersList.value = detailUser
                } else {
                    _errorMsg.value = "Error: ${response.message()}"
                    Log.e(ERROR_PLACE, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                _errorMsg.value = "Error: ${t.message}"
                Log.e(ERROR_PLACE, "onFailure: ${t.message}")
            }
        })
    }

    internal fun getFollowingFromTheUser(usernameQuery: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getFollowingUser("token $TOKEN", username = usernameQuery)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val detailUser = response.body()
//                    whether null or empty, assign it
                        _userFollowingList.value = detailUser
                } else {
                    _errorMsg.value = "Error: ${response.message()}"
                    Log.e(ERROR_PLACE, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                _errorMsg.value = "Error: ${t.message}"
                Log.e(ERROR_PLACE, "onFailure: ${t.message}")
            }
        })
    }
}