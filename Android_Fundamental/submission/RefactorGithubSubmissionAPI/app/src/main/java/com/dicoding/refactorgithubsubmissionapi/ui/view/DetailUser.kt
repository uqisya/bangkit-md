package com.dicoding.refactorgithubsubmissionapi.ui.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.refactorgithubsubmissionapi.R
import com.dicoding.refactorgithubsubmissionapi.data.remote.response.DetailUserResponse
import com.dicoding.refactorgithubsubmissionapi.databinding.ActivityDetailUserBinding
import com.dicoding.refactorgithubsubmissionapi.adapter.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUser : AppCompatActivity() {

    companion object {
        const val DATA_ACCOUNT = "DATA"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.github_follower,
            R.string.github_following
        )
    }

    private lateinit var binding: ActivityDetailUserBinding

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

        if (account != null) {
            Glide.with(this@DetailUser)
                .load(account.avatarUrl)
                .into(binding.imgvDetailUser)
            binding.tvAccountName.text = account.name
            binding.tvAccountUsername.text = account.login
            binding.tvAccountFollower.text = "${account.followers} Followers"
            binding.tvAccountFollowing.text = "${account.following} Following"
        }

        setSectionsPage(account)
        supportActionBar?.elevation = 0f
    }

    private fun setSectionsPage(account: DetailUserResponse?){
        val sectionsPageAdapter = SectionsPagerAdapter(this@DetailUser)
        sectionsPageAdapter.username = account?.login.toString()
        val viewPager2: ViewPager2 = binding.viewPagerDetail
        viewPager2.adapter = sectionsPageAdapter

        val tabs: TabLayout = binding.tabsLayFollowerFollowingDetail
        TabLayoutMediator(tabs, viewPager2) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }
}