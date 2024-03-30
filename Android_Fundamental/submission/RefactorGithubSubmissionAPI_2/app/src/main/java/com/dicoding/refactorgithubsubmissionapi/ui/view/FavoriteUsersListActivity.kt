package com.dicoding.refactorgithubsubmissionapi.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.refactorgithubsubmissionapi.adapter.UsersListAdapter
import com.dicoding.refactorgithubsubmissionapi.data.remote.response.ItemsItem
import com.dicoding.refactorgithubsubmissionapi.databinding.ActivityFavoriteUsersListBinding
import com.dicoding.refactorgithubsubmissionapi.ui.view_model.FavoriteUserViewModel
import com.dicoding.refactorgithubsubmissionapi.ui.view_model.MainViewModel
import com.dicoding.refactorgithubsubmissionapi.factory.ViewModelFactory

class FavoriteUsersListActivity : AppCompatActivity() {

    private lateinit var favUserViewModel: FavoriteUserViewModel
    private lateinit var mainViewModel: MainViewModel

    private lateinit var binding: ActivityFavoriteUsersListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUsersListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set layout manager untuk RecyclerView yg ada di activity_main.xml
        val linearLayoutManager = LinearLayoutManager(this@FavoriteUsersListActivity)
        binding.rvFavoriteUsersAccount.layoutManager = linearLayoutManager

        // buat garis pemisah antar item di RecyclerView
        val itemDecoration = DividerItemDecoration(this@FavoriteUsersListActivity, linearLayoutManager.orientation)
        binding.rvFavoriteUsersAccount.addItemDecoration(itemDecoration)

        mainViewModel = ViewModelFactory.getViewModel(this@FavoriteUsersListActivity, MainViewModel::class.java)

        mainViewModel.userDetail.observe(this@FavoriteUsersListActivity) { event ->
            event.getContentIfNotHandled()?.let { user ->
                val moveDetailUserActivityIntent = Intent(this@FavoriteUsersListActivity, DetailUserActivity::class.java)
                moveDetailUserActivityIntent.putExtra("DATA", user)
                startActivity(moveDetailUserActivityIntent)
            }
        }

        mainViewModel.isLoading.observe(this@FavoriteUsersListActivity) { value ->
            if (value == true) binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.GONE
        }

        favUserViewModel = ViewModelFactory.getViewModel(this@FavoriteUsersListActivity, FavoriteUserViewModel::class.java)
//        sebelum ditampilin ke recyclerview, perlu ubah dulu object dari FavoriteUser ke ItemsItem
        favUserViewModel.favoriteUsersList.observe(this@FavoriteUsersListActivity) { users ->
            val items = arrayListOf<ItemsItem>()
            users.map {  user ->
                val item = ItemsItem(login = user.username, avatarUrl = user.avatarUrl)
                items.add(item)
            }
            setUsersListDataToRecyclerView(items.toList())
        }
    }

    private fun setUsersListDataToRecyclerView(usersListAccount: List<ItemsItem>) {
        val adapter = UsersListAdapter()
        adapter.submitList(usersListAccount)

        binding.rvFavoriteUsersAccount.adapter = adapter

        adapter.setOnItemClickListener(object : UsersListAdapter.OnItemClickListener {
            override fun onItemClick(userAccount: ItemsItem) {
                mainViewModel.getGithubUserDetailAccount(usernameQuery = userAccount.login.toString())
            }
        })
    }
}