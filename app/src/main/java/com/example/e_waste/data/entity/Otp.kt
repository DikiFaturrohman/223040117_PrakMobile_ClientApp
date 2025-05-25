package com.example.e_waste.data.entity

// Otp.kt
@Entity(tableName = "otps")
data class OtpEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val email: String,
    val otp: String,
    val expiresAt: Long,
    val isVerified: Boolean = false
)