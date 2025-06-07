package com.example.e_waste.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.e_waste.data.dao.EWasteDao
import com.example.e_waste.data.dao.UserDao
import com.example.e_waste.data.entity.EWasteEntity
import com.example.e_waste.data.entity.UserEntity

@Database(
    entities = [UserEntity::class, EWasteEntity::class], // OtpEntity dihapus
    version = 2, // Jika Anda sudah merilis aplikasi, Anda perlu menaikkan versi dan menambahkan migrasi
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun eWasteDao(): EWasteDao
    // abstract fun otpDao(): OtpDao dihapus
}