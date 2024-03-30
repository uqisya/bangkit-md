package com.dicoding.refactorgithubsubmissionapi.ui.view

import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.refactorgithubsubmissionapi.R
import com.dicoding.refactorgithubsubmissionapi.databinding.ActivityConfigBinding
import com.google.android.material.switchmaterial.SwitchMaterial

class ConfigActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfigBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val switchTheme: SwitchMaterial = binding.switchTheme

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }
    }
}