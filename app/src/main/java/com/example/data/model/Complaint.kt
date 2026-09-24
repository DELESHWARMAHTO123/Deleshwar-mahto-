package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "complaints")
data class Complaint(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val filedByUserId: String,
    val filedByName: String,
    val filedByRole: String, // "golfer", "caddie", "staff"
    val targetPersonName: String,
    val targetPersonRole: String, // "golfer", "caddie", "staff"
    val category: String, // "Conduct / Etiquette", "Punctuality", etc.
    val description: String,
    val dateFiled: String,
    val status: String = "Pending Review", // "Pending Review", "Under Investigation", "Resolved", "Dismissed"
    val adminResolutionNotes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
