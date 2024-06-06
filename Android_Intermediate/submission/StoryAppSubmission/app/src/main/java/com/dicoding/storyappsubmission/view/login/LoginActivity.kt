package com.dicoding.storyappsubmission.view.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.storyappsubmission.data.ResultState
import com.dicoding.storyappsubmission.databinding.ActivityLoginBinding
import com.dicoding.storyappsubmission.utils.UserPreferences
import com.dicoding.storyappsubmission.utils.showToast
import com.dicoding.storyappsubmission.view.main.MainActivity
import com.dicoding.storyappsubmission.view.welcome.WelcomeActivity
import com.dicoding.storyappsubmission.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory= ViewModelFactory.getInstance(this@LoginActivity)
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        binding.loginButton.setOnClickListener {
            loginAccount()
        }
    }

    private fun loginAccount() {
        val (email, password) = getDataUserFromForm()

        viewModel.loginAccount(email, password).observe(this@LoginActivity) { resultState ->
            if (resultState != null) {
                when (resultState) {
                    is ResultState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.loginButton.isEnabled = false
                    }
                    is ResultState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.loginButton.isEnabled = true
                        showToast(this@LoginActivity, resultState.data.loginResult.name)
                        lifecycleScope.launch {
                            viewModel.saveAuthToken(resultState.data.loginResult.token)
                            val intent = Intent(this@LoginActivity, WelcomeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                    }
                    is ResultState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.loginButton.isEnabled = true
                        showToast(this@LoginActivity, resultState.errorMessage)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun getDataUserFromForm(): Pair<String, String> {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        return Pair(email, password)
    }
}