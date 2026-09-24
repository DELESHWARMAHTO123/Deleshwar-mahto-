package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "private_chart")
data class PrivateChartEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val date: String,
    val roundTitle: String,
    val holesOrCourse: String,
    val scoreOrPar: String = "",
    val caddieOrGolferNotes: String = "",
    val privateTime: String = "",
    val personalNotes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
