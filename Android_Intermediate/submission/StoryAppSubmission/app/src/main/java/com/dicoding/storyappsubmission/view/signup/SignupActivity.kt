package com.dicoding.storyappsubmission.view.signup

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyappsubmission.data.ResultState
import com.dicoding.storyappsubmission.data.model.UserModel
import com.dicoding.storyappsubmission.databinding.ActivitySignupBinding
import com.dicoding.storyappsubmission.utils.showToast
import com.dicoding.storyappsubmission.viewmodel.ViewModelFactory

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var viewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory= ViewModelFactory.getInstance(this@SignupActivity)
        viewModel = ViewModelProvider(this, viewModelFactory)[SignupViewModel::class.java]

        binding.signupButton.setOnClickListener {
            registerAccount()
        }
    }

    private fun registerAccount() {
        val user = getDataUserFromForm()

        viewModel.registerAccount(user).observe(this@SignupActivity) { resultState ->
            if (resultState != null) {
                when (resultState) {
                    is ResultState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.signupButton.isEnabled = false
                    }
                    is ResultState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.signupButton.isEnabled = true
                        showToast(this@SignupActivity, resultState.data.message)
                    }
                    is ResultState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.signupButton.isEnabled = true
                        showToast(this@SignupActivity, resultState.errorMessage)
                    }
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

}