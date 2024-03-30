package com.dicoding.refactorgithubsubmissionapi.ui.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.refactorgithubsubmissionapi.R
import com.dicoding.refactorgithubsubmissionapi.data.remote.response.DetailUserResponse
import com.dicoding.refactorgithubsubmissionapi.databinding.ActivityDetailUserBinding
import com.dicoding.refactorgithubsubmissionapi.adapter.SectionsPagerAdapter
import com.dicoding.refactorgithubsubmissionapi.database.FavoriteUser
import com.dicoding.refactorgithubsubmissionapi.ui.view_model.DetailUserViewModel
import com.dicoding.refactorgithubsubmissionapi.ui.view_model.FavoriteUserViewModel
import com.dicoding.refactorgithubsubmissionapi.ui.view_model_factory.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    companion object {
        const val DATA_ACCOUNT = "DATA"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.github_follower,
            R.string.github_following
        )
    }

    private lateinit var binding: ActivityDetailUserBinding

//    fav user
    private var favUser: FavoriteUser? = null
    private lateinit var detailUserViewModel: DetailUserViewModel
    private var isFavUserStatus: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val account = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<DetailUserResponse>(DATA_ACCOUNT, DetailUserResponse::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<DetailUserResponse>(DATA_ACCOUNT)
        }

        detailUserViewModel = getViewModel(this@DetailUserActivity)
        if (account != null) {
            Glide.with(this@DetailUserActivity)
                .load(account.avatarUrl)
                .into(binding.imgvDetailUser)
            binding.tvAccountName.text = account.name
            binding.tvAccountUsername.text = account.login
            binding.tvAccountFollower.text = "${account.followers} Followers"
            binding.tvAccountFollowing.text = "${account.following} Following"

            favUser = setDataUser(account)

            detailUserViewModel.getFavoriteUserByUsername(favUser!!).observe(this@DetailUserActivity) { user ->
                if (user != null) {
                    isFavUserStatus = true
                    binding.fabFavorite.setImageResource(R.drawable.ic_favorite_24dp)
                } else {
                    isFavUserStatus = false
                    binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border_24dp)
                }
            }
        }

        setSectionsPage(account)
        supportActionBar?.elevation = 0f

//        floating action button favorite
        binding.fabFavorite.setOnClickListener {
            if (isFavUserStatus) {
                detailUserViewModel.deleteUser(favUser as FavoriteUser)
                showToast("Success deleted user from favorite list")
            } else {
                detailUserViewModel.addUser(favUser as FavoriteUser)
                showToast("Success add user to favorite list")
            }
        }
    }

    private fun getViewModel(activity: AppCompatActivity): DetailUserViewModel {
        val factory = ViewModelFactory.getDatabaseInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailUserViewModel::class.java]
    }

    private fun showToast(msg: String) {
        Toast.makeText(this@DetailUserActivity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun setDataUser(user: DetailUserResponse): FavoriteUser {
        favUser = FavoriteUser()
        favUser?.username = user.login.toString()
        favUser?.avatarUrl = user.avatarUrl

        return favUser as FavoriteUser
    }

    private fun setSectionsPage(account: DetailUserResponse?){
        val sectionsPageAdapter = SectionsPagerAdapter(this@DetailUserActivity)
        sectionsPageAdapter.username = account?.login.toString()
        val viewPager2: ViewPager2 = binding.viewPagerDetail
        viewPager2.adapter = sectionsPageAdapter

        val tabs: TabLayout = binding.tabslayoutFollowerFollowingDetail
        TabLayoutMediator(tabs, viewPager2) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }
}