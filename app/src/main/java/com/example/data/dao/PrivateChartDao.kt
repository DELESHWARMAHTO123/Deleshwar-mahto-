package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.PrivateChartEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface PrivateChartDao {
    @Query("SELECT * FROM private_chart WHERE userId = :userId ORDER BY date DESC, id DESC")
    fun getPrivateEntriesForUser(userId: String): Flow<List<PrivateChartEntry>>

    @Query("SELECT * FROM private_chart WHERE userId = :userId ORDER BY date DESC, id DESC")
    fun getPrivateChartsForUser(userId: String): Flow<List<PrivateChartEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrivateEntry(entry: PrivateChartEntry): Long

    @Update
    suspend fun updatePrivateEntry(entry: PrivateChartEntry)

    @Delete
    suspend fun deletePrivateEntry(entry: PrivateChartEntry)

    @Query("DELETE FROM private_chart WHERE id = :id AND userId = :userId")
    suspend fun deletePrivateEntry(id: Long, userId: String)
}
