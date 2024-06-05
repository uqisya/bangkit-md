package com.dicoding.storyappsubmission.view.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.storyappsubmission.R
import com.dicoding.storyappsubmission.data.local.model.UserModel
import com.dicoding.storyappsubmission.data.remote.response.ErrorResponse
import com.dicoding.storyappsubmission.data.remote.retrofit.ApiConfig
import com.dicoding.storyappsubmission.databinding.ActivitySignupBinding
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupButton.setOnClickListener {
            registerAccount()
        }
    }

    private fun registerAccount() {
        showLoading(true)

        // use lifecycleScope because we use suspend function in the ApiService.registerAccount
        // but if you just use regular function (not used suspend function), you can use enqueue callback, and it will need to override onResponse and onFailure
        lifecycleScope.launch {
            try {
                val user = getDataUserFromForm()

                // nanti harus dibuat kayak gini yaaa
//                val message = repository.register(name, email, password).message

                val apiService = ApiConfig.getApiService()
                val successResponse = apiService.registerAccount(
                    user.name,
                    user.email,
                    user.password
                )

                showLoading(false)
                showToast(successResponse.message)
            } catch (e: HttpException) {
                val errorResponse = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(errorResponse, ErrorResponse::class.java)
                showLoading(false)
                errorBody.message?.let { msg ->
                    showToast(msg)
                }
            }
        }

    }

    private fun getDataUserFromForm(): UserModel {
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        return UserModel(name, email, password)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}