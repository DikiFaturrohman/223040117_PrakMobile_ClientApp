package com.example.e_waste.data.dao

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