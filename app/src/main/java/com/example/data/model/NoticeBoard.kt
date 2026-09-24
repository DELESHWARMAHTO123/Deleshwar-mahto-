package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notice_posts")
data class NoticePost(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val authorId: String,
    val authorName: String,
    val authorRole: String, // "golfer", "caddie", "admin"
    val title: String,
    val message: String,
    val datePosted: String,
    val isPinned: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "notice_replies")
data class NoticeReply(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val postId: Long,
    val authorId: String,
    val authorName: String,
    val authorRole: String,
    val replyText: String,
    val dateReplied: String,
    val createdAt: Long = System.currentTimeMillis()
)
