package com.dicoding.storyappsubmission.view.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyappsubmission.databinding.ActivityWelcomeBinding
import com.dicoding.storyappsubmission.factory.ViewModelFactory
import com.dicoding.storyappsubmission.view.login.LoginActivity
import com.dicoding.storyappsubmission.view.login.LoginViewModel
import com.dicoding.storyappsubmission.view.main.MainActivity
import com.dicoding.storyappsubmission.view.signup.SignupActivity

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

        binding.loginButton.setOnClickListener {
            val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.signupButton.setOnClickListener {
            val intent = Intent(this@WelcomeActivity, SignupActivity::class.java)
            startActivity(intent)
        }

        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.welcomeThumbnailImageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val thumbnail = ObjectAnimator.ofFloat(binding.welcomeThumbnailImageView, View.ALPHA, 0f, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 0f, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 0f, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 0f, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.descTextView, View.ALPHA, 0f, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }

        AnimatorSet().apply {
            playSequentially(thumbnail, title, desc, together)
            start()
        }
    }
}