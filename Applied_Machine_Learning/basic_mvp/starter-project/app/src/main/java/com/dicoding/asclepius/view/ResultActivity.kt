package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.asclepius.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    companion object {
        const val IMAGE_URI = "EXTRA_IMAGE_URI"
        const val RESULT = "EXTRA_RESULT"
        const val ACCURACY = "EXTRA_ACCURACY"
    }

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO (29): get data from sender intent
        val imageUri = intent.getStringExtra(IMAGE_URI)
        val result = intent.getStringExtra(RESULT)
        val accuracy = intent.getFloatExtra(ACCURACY, 0f)

        // TODO (30): Menampilkan hasil gambar, prediksi, dan confidence score.
        binding.resultImage.setImageURI(Uri.parse(imageUri))

        binding.resultText.text = "$result $accuracy%"
    }


}