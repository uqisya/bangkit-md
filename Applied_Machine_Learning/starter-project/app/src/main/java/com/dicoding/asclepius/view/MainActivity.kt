package com.dicoding.asclepius.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.yalantis.ucrop.UCrop
import java.io.File

// TODO (25): implement ClassifierListener buat dapatin onResults maupun onError
class MainActivity : AppCompatActivity(), ImageClassifierHelper.ClassifierListener {

    companion object {
        const val IMAGE_URI = "EXTRA_IMAGE_URI"
        const val RESULT = "EXTRA_RESULT"
        const val ACCURACY = "EXTRA_ACCURACY"

        const val SELECTED_FILE_NAME = "EXTRA_SELECTED_FILE_NAME"
    }

    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null
    private var selectedFileName: String = ""

    private var imageClassifierHelper: ImageClassifierHelper? = null
    private var classificationLabel: String? = null
    private var classificationAccuracy: Float? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(IMAGE_URI, currentImageUri)
        outState.putString(SELECTED_FILE_NAME, selectedFileName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.removeButton.setOnClickListener {
            removeImage()
        }

        // TODO (4): click button gallery, then call startGalery
        binding.galleryButton.setOnClickListener {
            startGallery()
        }

        binding.analyzeButton.setOnClickListener {
            analyzeImage()
        }

        binding.analyzeButton.isEnabled = false
        binding.removeButton.isEnabled = false

        imageClassifierHelper = ImageClassifierHelper(
            context = this@MainActivity,
            classifierListener = this@MainActivity
        )

        // if device rotate
        if (savedInstanceState != null) {
            currentImageUri = savedInstanceState.getParcelable(IMAGE_URI)
            binding.previewImageView.setImageURI(currentImageUri)

            selectedFileName = savedInstanceState.getString(SELECTED_FILE_NAME, "")
            binding.valueFileNameTV.text = selectedFileName

            binding.analyzeButton.isEnabled = true
            binding.removeButton.isEnabled = true
            binding.infoInstructionTv.visibility = View.GONE
        }
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

    private fun removeImage() {
        // set preImgView to null (clear the ImageView)
        binding.previewImageView.setImageURI(null)

        // set uri to null (clear the uri)
        currentImageUri = null

        // disable again the analyze button
        binding.analyzeButton.isEnabled = false

        // set selected file name displayed to empty (clear the displayed)
        binding.valueFileNameTV.text = ""

        // set btn to disable
        binding.removeButton.isEnabled  = false
        binding.infoInstructionTv.visibility = View.VISIBLE
    }

    // TODO (5): Menampilkan gambar sesuai Gallery yang dipilih.
    private fun showImage() {
        currentImageUri?.let { imgUri ->
            Log.d("Image URI", "showImage: $imgUri")
            binding.previewImageView.setImageURI(imgUri)

            // extract file name from the uri
            selectedFileName = getFileName(imgUri)
            binding.valueFileNameTV.text = selectedFileName

            // set enable analyze button
            binding.analyzeButton.isEnabled = true
            binding.removeButton.isEnabled = true

            binding.infoInstructionTv.visibility = View.GONE

            cropImage(imgUri)
        }
    }

    private fun getFileName(imgUri: Uri): String {
        var name = "unknown"
        val cursor = contentResolver.query(imgUri, null, null, null, null)
        cursor?.use { curCursor ->
            if (curCursor.moveToFirst()) {
                val nameIndex = curCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    name = curCursor.getString(nameIndex)
                }
            }
        }
        return name
    }

    private fun cropImage(imgUri: Uri) {
        val desUri = Uri.fromFile(
            File(cacheDir, "cropped")
        )
        UCrop.of(imgUri, desUri)
            .start(this@MainActivity)
    }

    @Deprecated("Deprecated in Java")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)

            // update currentImageUri to cropped version
            currentImageUri = resultUri

            // display cropped image
            binding.previewImageView.setImageURI(resultUri)
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)

            // error crop image
            Toast.makeText(this@MainActivity, "Crop image error: $cropError", Toast.LENGTH_LONG).show()
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