package com.dicoding.refactorgithubsubmissionapi.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.refactorgithubsubmissionapi.data.remote.response.ItemsItem
import com.dicoding.refactorgithubsubmissionapi.databinding.ActivityMainBinding
import com.dicoding.refactorgithubsubmissionapi.adapter.UsersListAdapter
import com.dicoding.refactorgithubsubmissionapi.ui.view_model.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    /*
    * start from here onCreate() <- sesuai life cycle android
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)  // set content activity ke view

        supportActionBar?.hide() // hide action bar (yg ada di atas seperti navbar, isinya judul activity)

        // set layout manager untuk RecyclerView yg ada di activity_main.xml
        val linearLayoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvListGithubAccount.layoutManager = linearLayoutManager

        // buat garis pemisah antar item di RecyclerView
        val itemDecoration = DividerItemDecoration(this@MainActivity, linearLayoutManager.orientation)
        binding.rvListGithubAccount.addItemDecoration(itemDecoration)

        // dapetin ViewModel
        mainViewModel = ViewModelProvider(this@MainActivity, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        /*
        * kalo ada perubahan data di isLoading (MainViewModel), maka bakal lakuin blok kode yg ada di observe
        * showLoading(value) -> buat ganti status apakah progress bar nya visible atau engga
         */
        mainViewModel.isLoading.observe(this@MainActivity) { value ->
            if (value == true) binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.GONE
        }

        mainViewModel.noDataVisibility.observe(this@MainActivity) { value ->
            if (value == true) binding.tvNoData.visibility = View.VISIBLE
            else binding.tvNoData.visibility = View.GONE
        }

        /*
        * observe data pada MainViewModel
        * intinya observe itu kayak bakal tau kalau di usersList itu ada perubahan
        * maka bakal lakuin blok kode yang ada di observe
         */
        mainViewModel.usersList.observe(this@MainActivity) { usersListData ->
            // di sini nanti bakal panggil UsersListAdapter
            // intinya nanti live data (usersList) akan dikirim ke ListAdapter buat ditampilin ke view
            setUsersListDataToRecyclerView(usersListData)
        }

        mainViewModel.errorMsg.observe(this@MainActivity) { message ->
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
        }

        mainViewModel.userDetail.observe(this@MainActivity) { event ->
            event.getContentIfNotHandled()?.let { user ->
                val moveDetailUserIntent = Intent(this@MainActivity, DetailUser::class.java)
                moveDetailUserIntent.putExtra("DATA", user)
                startActivity(moveDetailUserIntent)
            }
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        var usernameInputtedText = searchView.editText.text.toString()
                        if (usernameInputtedText.isEmpty()) {
                            usernameInputtedText = MainViewModel.USERNAME_DEFAULT
                        }
                        mainViewModel.findGithubUsersListAccount(usernameQuery = usernameInputtedText)
                        searchBar.setText(usernameInputtedText)
                        searchView.editText.setText(usernameInputtedText)
                        searchView.hide()  // hide searchView
                        return@setOnEditorActionListener true
                    }
                    searchView.hide()  // hide searchView
                    return@setOnEditorActionListener false
                }
        }

    }

    private fun setUsersListDataToRecyclerView(usersListAccount: List<ItemsItem?>?) {
        val adapter = UsersListAdapter()
        adapter.submitList(usersListAccount)

        binding.rvListGithubAccount.adapter = adapter

        adapter.setOnItemClickListener(object : UsersListAdapter.OnItemClickListener {
            override fun onItemClick(userAccount: ItemsItem) {
                mainViewModel.getGithubUserDetailAccount(usernameQuery = userAccount.login.toString())
            }
        })
    }
}