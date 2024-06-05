package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper

// TODO (25): implement ClassifierListener buat dapatin onResults maupun onError
class MainActivity : AppCompatActivity(), ImageClassifierHelper.ClassifierListener {

    companion object {
        const val IMAGE_URI = "EXTRA_IMAGE_URI"
        const val RESULT = "EXTRA_RESULT"
        const val ACCURACY = "EXTRA_ACCURACY"
    }

    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null

    private var imageClassifierHelper: ImageClassifierHelper? = null
    private var classificationLabel: String? = null
    private var classificationAccuracy: Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO (4): click button gallery, then call startGalery
        binding.galleryButton.setOnClickListener {
            startGallery()
        }

        binding.analyzeButton.setOnClickListener {
            analyzeImage()
        }

        imageClassifierHelper = ImageClassifierHelper(
            context = this@MainActivity,
            classifierListener = this@MainActivity
        )
    }

    // TODO (2): launcher to start photo picker and handle the result
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            // TODO (6): handle selected image URI
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    // TODO (3): Mendapatkan gambar dari Gallery.
    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(
            ActivityResultContracts.PickVisualMedia.ImageOnly
        ))
    }

    // TODO (5): Menampilkan gambar sesuai Gallery yang dipilih.
    private fun showImage() {
        currentImageUri.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    // TODO (24): Menganalisa gambar yang berhasil ditampilkan.
    private fun analyzeImage() {
        currentImageUri?.let { imageUri ->
            val (label, accuracy) = classifyImage(imageUri)
            // TODO (28): passing data to result activity
            moveToResult(imageUri, label, accuracy)
        }?:showToast("Silahkan masukkan berkas gambar terlebih dahulu")
    }

    private fun moveToResult(imageUri: Uri, result: String, accuracy: Float) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(IMAGE_URI, imageUri.toString())
        intent.putExtra(RESULT, result)
        intent.putExtra(ACCURACY, accuracy)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // TODO (26): dapatin accuracy dari ImageClassifierHelper
    private fun classifyImage(imageUri: Uri): Pair<String, Float> {
        imageClassifierHelper?.classifyStaticImage(imageUri)
        return Pair(classificationLabel ?: "Unknown", classificationAccuracy ?: 0f)
    }

    override fun onError(error: String) {

    }

    // TODO (27): assign result label, accuracy
    override fun onResults(resultLabel: String, accuracy: Float) {
        classificationAccuracy = accuracy
        classificationLabel = resultLabel
    }
}