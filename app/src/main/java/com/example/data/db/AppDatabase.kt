package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.ComplaintDao
import com.example.data.dao.DailyChartDao
import com.example.data.dao.NoticeBoardDao
import com.example.data.dao.PrivateChartDao
import com.example.data.dao.UserDao
import com.example.data.model.Complaint
import com.example.data.model.DailyChartEntry
import com.example.data.model.NoticePost
import com.example.data.model.NoticeReply
import com.example.data.model.PrivateChartEntry
import com.example.data.model.UserProfile

@Database(
    entities = [
        UserProfile::class,
        DailyChartEntry::class,
        PrivateChartEntry::class,
        Complaint::class,
        NoticePost::class,
        NoticeReply::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun dailyChartDao(): DailyChartDao
    abstract fun privateChartDao(): PrivateChartDao
    abstract fun complaintDao(): ComplaintDao
    abstract fun noticeBoardDao(): NoticeBoardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "caddis_day_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
