package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.dicoding.asclepius.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.lang.IllegalStateException


// TODO (9): Tambah parameter image classifier
class ImageClassifierHelper(
    var threshold: Float = 0.1f,
    var maxResults: Int = 1,
    var modelName: String = "cancer_classification.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    // TODO (15): variable dan companion object imageClassifier
    private var imageClassifier: ImageClassifier? = null

    companion object {
        private const val classTag = "ImageClassifierHelper"
    }

    // TODO (11): panggil setupImageClassifier() pertama kali setiap kelas ini dibuat
    init {
        setupImageClassifier()
    }

    // TODO (10): buat listener (callback) untuk tau prosesnya berhasil/gagal. Berhasil -> panggil onResults, Gagal -> panggil onError
    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
            result: String,
            accuracy: Float)
    }

    // TODO (12): Menyiapkan Image Classifier untuk memproses gambar.
    private fun setupImageClassifier() {
        // TODO (13): atur configurations seperti nilai threshold, maks hasil, dan jumlah thread
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResults)

        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(4)

        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        // TODO (14): try catch buat kembaliin nilai apabila proses inisialisasi gagal
        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(classTag, e.message.toString())
        }
    }

    // TODO (16): mengklasifikasikan imageUri dari gambar statis.
    fun classifyStaticImage(imageUri: Uri) {
        if (imageClassifier == null) {
            setupImageClassifier()
        }

        // TODO (17): ImageProcessor buat lakuin preprocessing sesuai metadata model, yakni 224 x 224 dengan tipe data FLOAT32
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(CastOp(DataType.FLOAT32))
            .build()

        // TODO (22): call uriToBitmap()
        val copiedBitmap = uriToBitmap(imageUri, imageProcessor)

        copiedBitmap?.let { bitmap ->
            // TODO (23): Fungsi TensorImage.fromBitmap() digunakan untuk mengonversi Bitmap menjadi TensorImage
            val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))

            val results = imageClassifier?.classify(tensorImage)

            // TODO (24): accuracy passing ke classifierListener bagian onResults
            val topResult = results?.get(0)?.categories?.get(0)
            topResult?.let { category ->
                val accuracy = category.score * 100 // accuracy/confidenceScore
                val result = category.label // label result cancer or non-cancer
                classifierListener?.onResults(result, accuracy)
            }
        }

    }

    // TODO (18): perlu konversi Uri menjadi Bitmap terlebih dahulu untuk digunakan sebagai TensorImage
    private fun uriToBitmap(imageUri: Uri, imageProcessor: ImageProcessor): Bitmap? {
        // buat object bitmap
        // TODO (19): use ImageDecoder for Pie version or higher
        val bitmap: Bitmap? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            // TODO (20): use MediaStore to decode the image for lower than Pie version
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }
        // TODO (21): setelah Bitmap didapatkan, copy dengan Bitmap.Config.ARGB_8888 configuration
        // configuration ini allow setiap pixel to be stored on 4 bytes
        val copiedBitmap = bitmap?.copy(Bitmap.Config.ARGB_8888, true)

        return copiedBitmap
    }

}