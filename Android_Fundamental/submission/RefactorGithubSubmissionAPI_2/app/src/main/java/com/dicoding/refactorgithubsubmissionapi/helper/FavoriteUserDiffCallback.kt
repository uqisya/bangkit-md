package com.dicoding.refactorgithubsubmissionapi.helper

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.refactorgithubsubmissionapi.database.FavoriteUser

class FavoriteUserDiffCallback(private val oldFavUserList: List<FavoriteUser>, private val newFavUserList: List<FavoriteUser>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldFavUserList.size
    }

    override fun getNewListSize(): Int {
        return newFavUserList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavUserList[oldItemPosition].username == newFavUserList[newItemPosition].username
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFavUser = oldFavUserList[oldItemPosition]
        val newFavUser = newFavUserList[newItemPosition]

        return oldFavUser.username == newFavUser.username && oldFavUser.avatarUrl == newFavUser.avatarUrl
    }

}