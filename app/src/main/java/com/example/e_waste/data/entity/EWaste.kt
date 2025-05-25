package com.example.e_waste.data.entity

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