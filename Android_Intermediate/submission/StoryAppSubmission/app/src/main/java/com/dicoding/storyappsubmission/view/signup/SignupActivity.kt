package com.dicoding.storyappsubmission.view.signup

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyappsubmission.data.ResultState
import com.dicoding.storyappsubmission.data.model.UserModel
import com.dicoding.storyappsubmission.databinding.ActivitySignupBinding
import com.dicoding.storyappsubmission.utils.showToast
import com.dicoding.storyappsubmission.view.login.LoginActivity
import com.dicoding.storyappsubmission.factory.ViewModelFactory

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
                        moveToLogin(user)
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

    private fun moveToLogin(user: UserModel) {
        binding.statusMessageTextView.text = "Email ${user.email} berhasil terdaftar"
        binding.statusMessageTextView.visibility = View.VISIBLE

        val countdownTimer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.countdownTextView.visibility = View.VISIBLE
                binding.countdownTextView.text = "Redirecting to login page in ${millisUntilFinished / 1000} seconds..."
            }
            override fun onFinish() {
                startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                finish()
            }
        }

        countdownTimer.start()
    }

}