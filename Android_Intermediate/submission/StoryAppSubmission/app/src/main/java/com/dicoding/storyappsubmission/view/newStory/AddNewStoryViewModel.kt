package com.dicoding.storyappsubmission.view.newStory

import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyappsubmission.data.model.StoryModel
import com.dicoding.storyappsubmission.data.repository.AddNewStoryRepository
import com.dicoding.storyappsubmission.utils.LocationUserUtils
import kotlinx.coroutines.launch

class AddNewStoryViewModel(private val repository: AddNewStoryRepository) : ViewModel() {
    fun addNewStory(story: StoryModel) = repository.addNewStory(story)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    private val _userLocation = MutableLiveData<Location?>()
    val userLocation: LiveData<Location?> = _userLocation

    fun getUserLocation(context: Context) {
        viewModelScope.launch {
            val location: Location? = LocationUserUtils.getMyLastLocation(context)
            if (location != null) {
                _userLocation.value = location
            }
        }
    }

    fun clearUserLocation() {
        _userLocation.value = null
    }
}