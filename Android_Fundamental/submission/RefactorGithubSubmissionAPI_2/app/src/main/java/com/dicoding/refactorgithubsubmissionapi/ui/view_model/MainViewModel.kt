package com.dicoding.refactorgithubsubmissionapi.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.refactorgithubsubmissionapi.BuildConfig
import com.dicoding.refactorgithubsubmissionapi.data.remote.response.DetailUserResponse
import com.dicoding.refactorgithubsubmissionapi.data.remote.response.ItemsItem
import com.dicoding.refactorgithubsubmissionapi.data.remote.response.QueryUserResponse
import com.dicoding.refactorgithubsubmissionapi.data.remote.retrofit.ApiConfig
import com.dicoding.refactorgithubsubmissionapi.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    companion object {
        const val USERNAME_DEFAULT = "arif"
        private const val TOKEN = BuildConfig.API_KEY
        private const val ERROR_PLACE = "MainViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _usersList = MutableLiveData<List<ItemsItem?>?>()
    val usersList: LiveData<List<ItemsItem?>?> = _usersList

    private val _userDetail = MutableLiveData<Event<DetailUserResponse?>>()
    val userDetail: LiveData<Event<DetailUserResponse?>> = _userDetail

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg

    private val _noDataVisibility = MutableLiveData<Boolean>()
    val noDataVisibility: LiveData<Boolean> = _noDataVisibility

//    init {
//        findGithubUsersListAccount(USERNAME_DEFAULT)
//    }

    /*
    * mencari akun github default nya = "arif" <- username dari companion object
    * dia bakal mengubah/assign data baru dari hasil response.body() ke _usersList
     */
    internal fun findGithubUsersListAccount(usernameQuery: String){

        _isLoading.value = true // ubah progress bar jadi visible
        _noDataVisibility.value = false  // gone

        val client = ApiConfig.getApiService().getGithubUserAccount("token $TOKEN", username = usernameQuery)
        client.enqueue(object : Callback<QueryUserResponse> {
            override fun onResponse(call: Call<QueryUserResponse>, response: Response<QueryUserResponse>) {
                _isLoading.value = false  // ubah kembali tampilan loading jadi not visible (karena udah beres get data dari API nya)
                if (response.isSuccessful) {
                    val responseBody = response.body() // <- return QueryUserResponse
                    if (responseBody != null) {  // kalau ada data nya
                        _usersList.value = responseBody.items  // assign data users baru ke value _userList
                        // ini juga secara tidak langsung akan update value data di usersList
                        if (responseBody.items!!.isEmpty()) {
                            _noDataVisibility.value = true  // visible text no data di center
                        }
                    }
                } else {
                    _errorMsg.value = "Error: ${response.message()}"
                    Log.e(ERROR_PLACE, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<QueryUserResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMsg.value = "Error: ${t.message}"
                Log.e(ERROR_PLACE, "onFailure: ${t.message}")
            }

        })
    }

    internal fun getGithubUserDetailAccount(usernameQuery: String){
        _isLoading.value = true // ubah progress bar jadi visible
        _noDataVisibility.value = false  // gone

        val client = ApiConfig.getApiService().getDetailUser("token $TOKEN", username = usernameQuery)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(call: Call<DetailUserResponse>, response: Response<DetailUserResponse>) {
                _isLoading.value = false  // ubah kembali tampilan loading jadi not visible (karena udah beres get data dari API nya)
                if (response.isSuccessful) {
                    val userAccount = response.body() // <- return DetailUserResponse
                    if (userAccount != null) {  // kalau ada data nya
                        _userDetail.postValue(Event(userAccount)) // assign data users baru ke value _userDetail
                        // ini juga secara tidak langsung akan update value data di userDetail
                        if (userAccount.login.isNullOrEmpty()) {
                            _noDataVisibility.value = true  // visible text no data di center
                        }
                    }
                } else {
                    _errorMsg.value = "Error: ${response.message()}"
                    Log.e(ERROR_PLACE, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMsg.value = "Error: ${t.message}"
                Log.e(ERROR_PLACE, "onFailure: ${t.message}")
            }

        })
    }

    internal fun setIsLoadingValue(isLoading: Boolean) {
        _isLoading.value = isLoading
    }
}