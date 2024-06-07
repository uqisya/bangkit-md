package com.dicoding.storyappsubmission.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyappsubmission.data.remote.response.ListStoryItem
import com.dicoding.storyappsubmission.data.remote.response.StoryResponse
import com.dicoding.storyappsubmission.databinding.StoryItemBinding
import com.dicoding.storyappsubmission.utils.setLocalDateFormat

class StoryAdapter : ListAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryAdapter.MyViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    private lateinit var listener: OnItemClickListener
    override fun onBindViewHolder(holder: StoryAdapter.MyViewHolder, position: Int) {
        val storyItem = getItem(position)
        holder.bind(storyItem)

        holder.itemView.setOnClickListener {
            listener.onItemClick(storyItem)
        }
    }

    class MyViewHolder(private val binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(storyItem: ListStoryItem) {
            val localDate = setLocalDateFormat(storyItem.createdAt)
            binding.timeTextView.text = localDate

            binding.itemNameUserTextView.text = storyItem.name
            binding.itemDescriptionStoryTextView.text = storyItem.description
            Glide.with(itemView.context)
                .load(storyItem.photoUrl)
                .into(binding.thumbnailImageView)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(storyItem: ListStoryItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}