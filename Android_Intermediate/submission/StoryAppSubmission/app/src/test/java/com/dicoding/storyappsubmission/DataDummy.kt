package com.dicoding.storyappsubmission

import androidx.room.PrimaryKey
import com.dicoding.storyappsubmission.data.remote.response.ListStoryItem
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        val random = Random()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

        for (i in 0..100) {
            val randomLon = -180 + (180 - (-180)) * random.nextDouble()
            val randomLat = -90 + (90 - (-90)) * random.nextDouble()
            val randomDate = Date((946684800000L + ((Date().time - 946684800000L) * random.nextDouble())).toLong())
            val formattedDate = dateFormat.format(randomDate)

            val listStoryItem = ListStoryItem(
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic${i}.png",
                createdAt = formattedDate,
                name = "Syauqi $i",
                description = "Lorem Ipsum $i",
                lon = randomLon,
                id = i.toString(),
                lat = randomLat
            )
            items.add(listStoryItem)
        }
        return items
    }
}