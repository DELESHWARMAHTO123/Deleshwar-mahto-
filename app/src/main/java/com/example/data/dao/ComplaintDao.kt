package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Complaint
import kotlinx.coroutines.flow.Flow

@Dao
interface ComplaintDao {
    @Query("SELECT * FROM complaints ORDER BY createdAt DESC")
    fun getAllComplaints(): Flow<List<Complaint>>

    @Query("SELECT * FROM complaints WHERE filedByUserId = :userId ORDER BY createdAt DESC")
    fun getComplaintsFiledByUser(userId: String): Flow<List<Complaint>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaint(complaint: Complaint)

    @Update
    suspend fun updateComplaint(complaint: Complaint)

    @Query("DELETE FROM complaints WHERE id = :id")
    suspend fun deleteComplaint(id: Long)
}
