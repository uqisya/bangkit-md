package com.dicoding.storyappsubmission.view.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyappsubmission.databinding.ActivityWelcomeBinding
import com.dicoding.storyappsubmission.view.login.LoginActivity
import com.dicoding.storyappsubmission.view.login.LoginViewModel
import com.dicoding.storyappsubmission.view.main.MainActivity
import com.dicoding.storyappsubmission.view.signup.SignupActivity
import com.dicoding.storyappsubmission.factory.ViewModelFactory

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)

        supportActionBar?.hide()

        val viewModelFactory = ViewModelFactory.getInstance(this@WelcomeActivity)
        loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        loginViewModel.getAuthToken().observe(this@WelcomeActivity) { authToken ->
            if (authToken != null) {
                startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                finish()
            } else {
                setContentView(binding.root)
            }
        }

        // Set the alpha of the buttons and the title to 1 to make them visible
        binding.loginButton.alpha = 1f
        binding.signupButton.alpha = 1f
        binding.titleTextView.alpha = 1f
        binding.descTextView.alpha = 1f

        binding.loginButton.setOnClickListener {
            val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.signupButton.setOnClickListener {
            val intent = Intent(this@WelcomeActivity, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}