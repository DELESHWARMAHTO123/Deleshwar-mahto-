package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_chart")
data class DailyChartEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String, // YYYY-MM-DD
    val serialNumber: Int,
    val caddieName: String, // 1. Caddie's Name
    val caddieAadhaar: String = "", // 2. Caddie's Aadhaar No
    val caddieNumber: String = "", // 3. Caddie Number
    val golferName: String, // 4. Golfer Name
    val golferId: String = "",
    val caddieId: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val timeSlot: String = "07:30 AM",
    val autoFinishTime: String = "",
    val status: String = "Active", // "Active", "In Play", "Completed"
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
