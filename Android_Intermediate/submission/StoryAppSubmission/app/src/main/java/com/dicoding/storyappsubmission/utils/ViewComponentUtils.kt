package com.dicoding.storyappsubmission.utils

import android.content.Context
import android.widget.Toast
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun setLocalDateFormat(timestamp: String): String {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    val localDate = simpleDateFormat.parse(timestamp) as Date

    return DateFormat.getDateInstance(DateFormat.FULL).format(localDate)
}