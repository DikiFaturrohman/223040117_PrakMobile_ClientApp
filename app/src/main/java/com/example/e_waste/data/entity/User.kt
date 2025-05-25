package com.example.e_waste.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

// User.kt
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val email: String,
    val password: String,
    val name: String,
    val phone: String,
    val createdAt: Long = System.currentTimeMillis()
)