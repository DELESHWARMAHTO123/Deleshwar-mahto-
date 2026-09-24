package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.DailyChartEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyChartDao {
    @Query("SELECT * FROM daily_chart WHERE date = :date ORDER BY serialNumber ASC")
    fun getChartForDate(date: String): Flow<List<DailyChartEntry>>

    @Query("SELECT * FROM daily_chart WHERE date = :date ORDER BY serialNumber ASC")
    fun getEntriesForDate(date: String): Flow<List<DailyChartEntry>>

    @Query("SELECT DISTINCT date FROM daily_chart ORDER BY date DESC")
    fun getAllRecordedDates(): Flow<List<String>>

    @Query("SELECT * FROM daily_chart WHERE id = :id LIMIT 1")
    suspend fun getEntryById(id: Long): DailyChartEntry?

    @Query("SELECT COUNT(*) FROM daily_chart WHERE date = :date")
    suspend fun countEntriesForDate(date: String): Int

    @Query("SELECT COUNT(*) FROM daily_chart WHERE date = :date")
    suspend fun getCountForDate(date: String): Int

    @Query("SELECT caddieId FROM daily_chart WHERE date = :date")
    suspend fun getAssignedCaddieIdsForDate(date: String): List<String>

    @Query("SELECT * FROM daily_chart WHERE date = :date AND (golferName LIKE '%' || :query || '%' OR caddieName LIKE '%' || :query || '%') ORDER BY serialNumber ASC")
    fun searchChart(date: String, query: String): Flow<List<DailyChartEntry>>

    @Query("SELECT * FROM daily_chart WHERE golferName = :name OR caddieName = :name OR golferId = :userId OR caddieId = :userId ORDER BY date DESC, serialNumber ASC")
    fun getEntriesForUser(userId: String, name: String): Flow<List<DailyChartEntry>>

    @Query("SELECT * FROM daily_chart ORDER BY date DESC, serialNumber ASC")
    fun getAllChartEntries(): Flow<List<DailyChartEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: DailyChartEntry): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<DailyChartEntry>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntries(entries: List<DailyChartEntry>) {
        insertAll(entries)
    }

    @Update
    suspend fun updateEntry(entry: DailyChartEntry)
    
    // Note: Strictly no delete operations per user policy: immutable historical club record
}
