package com.dicoding.storyappsubmission.view.main

import android.content.Intent
import android.graphics.drawable.ColorDrawable
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
import com.dicoding.storyappsubmission.factory.ViewModelFactory
import com.dicoding.storyappsubmission.utils.showToast
import com.dicoding.storyappsubmission.view.adapter.StoryAdapter
import com.dicoding.storyappsubmission.view.newStory.AddNewStoryActivity
import com.dicoding.storyappsubmission.view.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBackgroundActionBar()
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getAllStories() {
        viewModel.getAllStories().observe(this) { resultState ->
            if (resultState != null) {
                when (resultState) {
                    is ResultState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is ResultState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        lifecycleScope.launch {
                            setStoriesListDataAdapter(resultState.data.listStory)
                        }
                    }
                    is ResultState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showToast(this@MainActivity, resultState.errorMessage)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setStoriesListDataAdapter(listStory: List<ListStoryItem?>) {
        val adapter = StoryAdapter()
        adapter.submitList(listStory)

        binding.listStoryRecyclerView.adapter = adapter
    }

    private fun setLayoutAdapter() {
        // set layout manager untuk recycler view
        val layoutManager = LinearLayoutManager(this)
        binding.listStoryRecyclerView.layoutManager = layoutManager
    }

    private fun setBackgroundActionBar() {
        val actionBar = supportActionBar
        val colorDrawable = ColorDrawable(resources.getColor(R.color.blue_white_soft))
        actionBar?.setBackgroundDrawable(colorDrawable)
    }
}