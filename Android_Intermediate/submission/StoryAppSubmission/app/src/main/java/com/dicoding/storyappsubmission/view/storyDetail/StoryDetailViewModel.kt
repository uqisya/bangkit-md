package com.dicoding.storyappsubmission.view.storyDetail

import androidx.lifecycle.ViewModel
import com.dicoding.storyappsubmission.data.repository.StoryDetailRepository
import com.dicoding.storyappsubmission.utils.UserPreferences

class StoryDetailViewModel(
    private val repository: StoryDetailRepository,
    private val userPref: UserPreferences
) : ViewModel() {
    fun getStoryDetail(storyID: String) = repository.getStoryDetail(storyID)
}