package com.dicoding.storyappsubmission.view.storyDetail

import androidx.lifecycle.ViewModel
import com.dicoding.storyappsubmission.data.repository.StoryDetailRepository

class StoryDetailViewModel(
    private val repository: StoryDetailRepository,
) : ViewModel() {
    fun getStoryDetail(storyID: String) = repository.getStoryDetail(storyID)
}