package com.dicoding.storyappsubmission.view.newStory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyappsubmission.data.model.StoryModel
import com.dicoding.storyappsubmission.data.repository.AddNewStoryRepository

class AddNewStoryViewModel(private val repository: AddNewStoryRepository) : ViewModel() {
    fun addNewStory(story: StoryModel) = repository.addNewStory(story)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }
}