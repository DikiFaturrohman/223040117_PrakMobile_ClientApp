package com.example.e_waste.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

// Otp.kt
@Entity(tableName = "otps")
data class OtpEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val email: String,
    val otp: String,
    val expiresAt: Long,
    val isVerified: Boolean = false
)