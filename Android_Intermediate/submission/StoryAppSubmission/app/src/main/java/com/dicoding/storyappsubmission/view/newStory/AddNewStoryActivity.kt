package com.dicoding.storyappsubmission.view.newStory

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyappsubmission.R
import com.dicoding.storyappsubmission.data.ResultState
import com.dicoding.storyappsubmission.data.model.StoryModel
import com.dicoding.storyappsubmission.databinding.ActivityAddNewStoryBinding
import com.dicoding.storyappsubmission.factory.ViewModelFactory
import com.dicoding.storyappsubmission.utils.getImageUri
import com.dicoding.storyappsubmission.utils.reduceFileImage
import com.dicoding.storyappsubmission.utils.showToast
import com.dicoding.storyappsubmission.utils.uriToFile
import com.dicoding.storyappsubmission.view.main.MainActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddNewStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNewStoryBinding
    private lateinit var viewModel: AddNewStoryViewModel

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val viewModelFactory = ViewModelFactory.getInstance(this@AddNewStoryActivity)
        viewModel = ViewModelProvider(this@AddNewStoryActivity, viewModelFactory)[AddNewStoryViewModel::class.java]

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.GONE
        }

        binding.galleryImageButton.setOnClickListener {
            startGallery()
        }

        binding.cameraImageButton.setOnClickListener {
            startCamera()
        }

        binding.uploadButton.setOnClickListener {
            uploadImage()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(
            PickVisualMediaRequest(
                ActivityResultContracts
                    .PickVisualMedia
                    .ImageOnly
            )
        )
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }


    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val story = getDataFromUser(uri)

            viewModel.addNewStory(story).observe(this) { resultState ->
                if (resultState != null) {
                    when (resultState) {
                        is ResultState.Loading -> {
                            viewModel.setLoading(true)
                            binding.uploadButton.isEnabled = false
                        }
                        is ResultState.Success -> {
                            viewModel.setLoading(false)
                            showToast(this@AddNewStoryActivity, resultState.data.message)
                            binding.uploadButton.isEnabled = true

                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        is ResultState.Error -> {
                            viewModel.setLoading(false)
                            showToast(this@AddNewStoryActivity, resultState.errorMessage)
                            binding.uploadButton.isEnabled = true
                        }
                    }
                }
            }
        } ?: showToast(this@AddNewStoryActivity, getString(R.string.empty_image_warning))
    }

    private fun getDataFromUser(uri: Uri): StoryModel {
        val imageFile = uriToFile(uri, this).reduceFileImage()
        val description = binding.descriptionEditText.text.toString()
        viewModel.setLoading(true)

        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        return StoryModel(multipartBody, requestBody)
    }
}