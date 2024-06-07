package com.dicoding.storyappsubmission.view.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyappsubmission.databinding.ActivityMainBinding
import com.dicoding.storyappsubmission.view.login.LoginActivity
import com.dicoding.storyappsubmission.view.signup.SignupActivity
import com.dicoding.storyappsubmission.view.welcome.WelcomeActivity
import com.dicoding.storyappsubmission.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private val authTokenObserver = Observer<String?> { authToken ->
        if (authToken == null) {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = ViewModelFactory.getInstance(this@MainActivity)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        viewModel.getAuthToken().observe(this, authTokenObserver)

        binding.logoutButton.setOnClickListener {
            viewModel.removeUserAuthToken()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.getAuthToken().removeObserver(authTokenObserver)
    }
}