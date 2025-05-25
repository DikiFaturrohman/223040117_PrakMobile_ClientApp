package com.example.e_waste.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.e_waste.data.entity.EWasteEntity
import kotlinx.coroutines.flow.Flow

// EWasteDao.kt
@Dao
interface EWasteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEWaste(eWaste: EWasteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEWastes(eWastes: List<EWasteEntity>)

    @Query("SELECT * FROM ewastes")
    fun getAllEWastes(): Flow<List<EWasteEntity>>

    @Query("SELECT * FROM ewastes WHERE category = :category")
    fun getEWastesByCategory(category: String): Flow<List<EWasteEntity>>
}