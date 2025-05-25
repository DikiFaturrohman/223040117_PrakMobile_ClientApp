package com.example.e_waste.data.dao

// OtpDao.kt
@Dao
interface OtpDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOtp(otp: OtpEntity)

    @Query("SELECT * FROM otps WHERE email = :email AND otp = :otp AND expiresAt > :currentTime AND isVerified = 0 LIMIT 1")
    suspend fun validateOtp(email: String, otp: String, currentTime: Long): OtpEntity?

    @Query("UPDATE otps SET isVerified = 1 WHERE id = :id")
    suspend fun markOtpAsVerified(id: String)
}