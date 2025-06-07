package com.example.e_waste.data.repository

import com.example.e_waste.data.api.ApiService
import com.example.e_waste.domain.model.Tip
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TipRepository @Inject constructor(
    private val apiService: ApiService
) {
    // Fungsi untuk mengambil data tips dari API
    suspend fun getTips(): Result<List<Tip>> {
        return try {
            val response = apiService.getTips()
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}