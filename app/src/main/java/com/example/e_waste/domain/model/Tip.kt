package com.example.e_waste.domain.model


// Cukup data class sederhana ini
data class Tip(
    val id: Int,
    val title: String,
    val content: String,
    // created_at dan updated_at bisa ditambahkan jika perlu ditampilkan
)