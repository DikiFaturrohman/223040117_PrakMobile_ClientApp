package com.example.e_waste.domain.model

// API Response Classes
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T
)

data class UserResponse(
    val id: String,
    val name: String,
    val email: String,
    val phone: String
)

data class TokenResponse(
    val token: String
)

data class OtpResponse(
    val message: String
)

data class VerifyOtpResponse(
    val verified: Boolean
)

data class ResetPasswordResponse(
    val success: Boolean
)

data class EWasteResponse(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val imageUrl: String?,
    val disposalMethod: String
)

data class CategoryResponse(
    val id: String,
    val name: String
)