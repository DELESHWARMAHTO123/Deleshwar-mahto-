package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.NoticePost
import com.example.data.model.NoticeReply
import kotlinx.coroutines.flow.Flow

@Dao
interface NoticeBoardDao {
    @Query("SELECT * FROM notice_posts ORDER BY isPinned DESC, createdAt DESC")
    fun getAllPosts(): Flow<List<NoticePost>>

    @Query("SELECT * FROM notice_replies WHERE postId = :postId ORDER BY createdAt ASC")
    fun getRepliesForPost(postId: Long): Flow<List<NoticeReply>>

    @Query("SELECT * FROM notice_replies ORDER BY createdAt ASC")
    fun getAllReplies(): Flow<List<NoticeReply>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: NoticePost): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReply(reply: NoticeReply): Long
}
