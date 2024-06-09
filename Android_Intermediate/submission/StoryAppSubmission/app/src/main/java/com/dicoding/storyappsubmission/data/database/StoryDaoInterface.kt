package com.dicoding.storyappsubmission.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.storyappsubmission.data.remote.response.ListStoryItem

@Dao
interface StoryDaoInterface {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListStory(listStory: List<ListStoryItem>)

    @Query("SELECT * FROM listStoryItem")
    fun getAllStories(): PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM listStoryItem")
    suspend fun deleteAllStory()
}