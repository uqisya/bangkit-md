package com.dicoding.storyappsubmission.view.storyMaps

import androidx.lifecycle.ViewModel
import com.dicoding.storyappsubmission.data.repository.ListStoryLocationRepository

class StoryMapsViewModel(private val repository: ListStoryLocationRepository) : ViewModel() {
    fun getAllStoriesWithLocation() = repository.getAllStoriesWithLocation()
}