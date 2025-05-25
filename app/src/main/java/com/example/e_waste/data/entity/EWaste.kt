package com.example.e_waste.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

// EWaste.kt
@Entity(tableName = "ewastes")
data class EWasteEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val category: String,
    val description: String,
    val imageUrl: String?,
    val disposalMethod: String
)