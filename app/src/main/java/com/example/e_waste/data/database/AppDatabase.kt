package com.example.e_waste.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.e_waste.data.dao.EWasteDao
import com.example.e_waste.data.dao.OtpDao
import com.example.e_waste.data.dao.UserDao
import com.example.e_waste.data.entity.UserEntity
import com.example.e_waste.data.entity.OtpEntity
import com.example.e_waste.data.entity.EWasteEntity

@Database(
    entities = [UserEntity::class, OtpEntity::class, EWasteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun otpDao(): OtpDao
    abstract fun eWasteDao(): EWasteDao
}