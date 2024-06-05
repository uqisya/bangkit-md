package com.dicoding.refactorgithubsubmissionapi.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.refactorgithubsubmissionapi.ui.view.UserFollowerFollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    companion object {
        const val POSITION = "position"
        const val USERNAME = "username"
    }

    var username: String = ""
    override fun createFragment(position: Int): Fragment {
        val fragment = UserFollowerFollowingFragment()
        fragment.arguments = Bundle().apply {
            putInt(POSITION, position + 1)
            putString(USERNAME, username)
        }
        return fragment
    }
}