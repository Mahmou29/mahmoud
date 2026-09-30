package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BriefDao {
    @Query("SELECT * FROM brief_history ORDER BY createdAt DESC")
    fun getAllBriefs(): Flow<List<BriefEntity>>

    @Query("SELECT * FROM brief_history WHERE id = :id LIMIT 1")
    suspend fun getBriefById(id: Long): BriefEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrief(brief: BriefEntity): Long

    @Update
    suspend fun updateBrief(brief: BriefEntity)

    @Delete
    suspend fun deleteBrief(brief: BriefEntity)

    @Query("DELETE FROM brief_history")
    suspend fun clearAll()
}
