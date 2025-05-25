package com.example.e_waste.data.database

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