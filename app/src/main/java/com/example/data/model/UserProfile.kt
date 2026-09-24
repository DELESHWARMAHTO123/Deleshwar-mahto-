package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserProfile(
    @PrimaryKey
    val id: String, // e.g., "G-101", "C-201", "ADMIN-01"
    val name: String,
    val role: String, // "golfer", "caddie", "admin"
    val phoneNumber: String,
    val address: String,
    val experienceOrHandicap: String = "", // e.g. "Handicap: 8" or "Caddie Exp: 4 yrs"
    val isAvailable: Boolean = true,
    val registeredDate: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
