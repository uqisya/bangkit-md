package com.dicoding.storyappsubmission.view.storyDetail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.storyappsubmission.data.ResultState
import com.dicoding.storyappsubmission.data.remote.response.Story
import com.dicoding.storyappsubmission.databinding.ActivityStoryDetailBinding
import com.dicoding.storyappsubmission.factory.ViewModelFactory
import com.dicoding.storyappsubmission.utils.setLocalDateFormat
import com.dicoding.storyappsubmission.utils.showToast

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding
    private lateinit var viewModel: StoryDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val storyID = intent.getStringExtra(STORY_ID)

        val viewModelFactory = ViewModelFactory.getInstance(this@StoryDetailActivity)
        viewModel = ViewModelProvider(this, viewModelFactory)[StoryDetailViewModel::class.java]

        if (storyID != null) {
            getStoryDetail(storyID)
        }
    }

    private fun getStoryDetail(storyID: String) {
        viewModel.getStoryDetail(storyID).observe(this) { resultState ->
            if (resultState != null) {
                when (resultState) {
                    is ResultState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is ResultState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        setDataToView(resultState.data.story)
                    }

                    is ResultState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showToast(this@StoryDetailActivity, resultState.errorMessage)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun setDataToView(story: Story) {
        binding.nameTextView.text = story.name

        val location = "${story.lat}, ${story.lon}"
        binding.locationTextView.text = location

        binding.descriptionTextView.text = story.description

        val localeDate = setLocalDateFormat(story.createdAt)
        binding.timeTextView.text = localeDate

        Glide.with(this)
            .load(story.photoUrl)
            .into(binding.thumbnailImageView)
    }

    companion object {
        const val STORY_ID = "story_id"
    }
}