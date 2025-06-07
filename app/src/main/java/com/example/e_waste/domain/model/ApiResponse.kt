package com.example.e_waste.domain.model

// Ini adalah model generic untuk semua respons dari server
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? // Data bisa null, contohnya saat logout
)

// Ini adalah model untuk data yang ada di dalam 'data' field
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String
)

data class EWaste(
    val id: Int,
    val name: String,
    val category: String,
    val description: String,
    val imageUrl: String?,
    val disposalMethod: String
)