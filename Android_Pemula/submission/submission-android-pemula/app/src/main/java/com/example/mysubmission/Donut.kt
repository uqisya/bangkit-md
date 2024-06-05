package com.example.mysubmission

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Donut(
    val name: String,
    val price: String,
    val stock: String,
    val description: String,
    val photo: Int
) : Parcelable
