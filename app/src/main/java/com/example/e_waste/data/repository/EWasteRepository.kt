package com.example.e_waste.data.repository

import com.example.e_waste.data.api.ApiService
import com.example.e_waste.data.dao.EWasteDao
import com.example.e_waste.data.entity.EWasteEntity
import com.example.e_waste.domain.model.EWaste
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EWasteRepository @Inject constructor(
    private val eWasteDao: EWasteDao,
    private val apiService: ApiService
) {
    fun getAllEWastes(): Flow<List<EWasteEntity>> {
        return eWasteDao.getAllEWastes()
    }

    // Fungsi untuk refresh data dari API
    suspend fun refreshEWastes() {
        try {
            val response = apiService.getEWastes()
            if (response.success && response.data != null) {
                val networkData = response.data.map { mapToEntity(it) }
                eWasteDao.insertAllEWastes(networkData)
            }
        } catch (e: Exception) {
            // Tangani error, misal tidak ada koneksi internet
            e.printStackTrace()
        }
    }

    private fun mapToEntity(eWaste: EWaste): EWasteEntity {
        return EWasteEntity(
            id = eWaste.id.toString(),
            name = eWaste.name,
            category = eWaste.category,
            description = eWaste.description,
            imageUrl = eWaste.imageUrl,
            disposalMethod = eWaste.disposalMethod
        )
    }
}