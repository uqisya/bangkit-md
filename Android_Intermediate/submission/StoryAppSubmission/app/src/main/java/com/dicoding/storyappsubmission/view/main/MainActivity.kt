package com.dicoding.storyappsubmission.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyappsubmission.R
import com.dicoding.storyappsubmission.data.ResultState
import com.dicoding.storyappsubmission.data.remote.response.ListStoryItem
import com.dicoding.storyappsubmission.databinding.ActivityMainBinding
import com.dicoding.storyappsubmission.utils.setBackgroundActionBar
import com.dicoding.storyappsubmission.utils.showToast
import com.dicoding.storyappsubmission.view.adapter.LoadingStateAdapter
import com.dicoding.storyappsubmission.view.adapter.StoryAdapter
import com.dicoding.storyappsubmission.view.factory.ViewModelFactory
import com.dicoding.storyappsubmission.view.newStory.AddNewStoryActivity
import com.dicoding.storyappsubmission.view.storyMaps.StoryMapsActivity
import com.dicoding.storyappsubmission.view.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBackgroundActionBar(this@MainActivity)
        supportActionBar?.show()

        setLayoutAdapter()

        val viewModelFactory = ViewModelFactory.getInstance(this@MainActivity)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        viewModel.getAuthToken().observe(this) { authToken ->
            if (authToken != null) {
                getAllStories()
            } else {
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.fabAddNewStory.setOnClickListener {
            val intent = Intent(this, AddNewStoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                viewModel.logoutAccount()
                true
            }
            R.id.action_story_maps -> {
                val intent = Intent(this, StoryMapsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getAllStories() {
        val adapter = getStoriesListDataAdapter()
        viewModel.getAllStories().observe(this) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }
    }

    private fun getStoriesListDataAdapter(): StoryAdapter {
        val adapter = StoryAdapter()
        binding.listStoryRecyclerView.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        return adapter
    }

    private fun setLayoutAdapter() {
        // set layout manager untuk recycler view
        val layoutManager = LinearLayoutManager(this)
        binding.listStoryRecyclerView.layoutManager = layoutManager
    }
}