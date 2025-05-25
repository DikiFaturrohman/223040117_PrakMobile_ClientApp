package com.example.e_waste.data.repository

import com.example.e_waste.data.api.ApiService
import com.example.e_waste.data.dao.EWasteDao
import com.example.e_waste.data.entity.EWasteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// EWasteRepository.kt
@Singleton
class EWasteRepository @Inject constructor(
    private val eWasteDao: EWasteDao,
    private val apiService: ApiService
) {
    fun getAllEWastes(): Flow<List<EWasteEntity>> {
        return eWasteDao.getAllEWastes()
    }

    fun getEWastesByCategory(category: String): Flow<List<EWasteEntity>> {
        return eWasteDao.getEWastesByCategory(category)
    }

    suspend fun refreshEWastes() {
        try {
            val response = apiService.getEWastes()
            if (response.success) {
                val eWastes = response.data.map {
                    EWasteEntity(
                        id = it.id,
                        name = it.name,
                        category = it.category,
                        description = it.description,
                        imageUrl = it.imageUrl,
                        disposalMethod = it.disposalMethod
                    )
                }
                eWasteDao.insertAllEWastes(eWastes)
            }
        } catch (e: Exception) {
            // Handle error
        }
    }

    suspend fun getEWasteCategories(): Result<List<String>> {
        return try {
            val response = apiService.getEWasteCategories()
            if (response.success) {
                Result.success(response.data.map { it.name })
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}