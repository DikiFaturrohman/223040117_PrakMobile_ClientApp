package com.example.e_waste.data.repository

import com.example.e_waste.data.api.ApiService
import com.example.e_waste.data.dao.OtpDao
import com.example.e_waste.data.dao.UserDao
import com.example.e_waste.data.database.AppDatabase
import com.example.e_waste.data.entity.UserEntity
import com.example.e_waste.domain.model.LoginRequest
import com.example.e_waste.domain.model.OtpRequest
import com.example.e_waste.domain.model.RegisterRequest
import com.example.e_waste.domain.model.ResetPasswordRequest
import com.example.e_waste.domain.model.VerifyOtpRequest
import javax.inject.Inject
import javax.inject.Singleton

// UserRepository.kt
@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val otpDao: OtpDao,
    private val apiService: ApiService,
    private val appDatabase: AppDatabase
) {
    suspend fun registerUser(name: String, email: String, password: String, phone: String): Result<UserEntity> {
        return try {
            val response = apiService.register(
                RegisterRequest(name, email, password, phone)
            )

            if (response.success) {
                val user = UserEntity(
                    email = email,
                    password = password,
                    name = name,
                    phone = phone
                )
                userDao.insertUser(user)
                Result.success(user)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val response = apiService.login(
                LoginRequest(email, password)
            )

            if (response.success) {
                Result.success(response.data.token)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserByEmail(email: String): UserEntity? {
        return try {
            userDao.getUserByEmail(email)
        } catch (e: Exception) {
            // Tangani error jika diperlukan, misalnya log atau return null
            // Log.e("UserRepository", "Error getting user by email", e)
            null
        }
    }

    suspend fun sendOtp(email: String): Result<Boolean> {
        return try {
            val response = apiService.sendOtp(OtpRequest(email))

            if (response.success) {
                Result.success(true)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verifyOtp(email: String, otp: String): Result<Boolean> {
        return try {
            val response = apiService.verifyOtp(VerifyOtpRequest(email, otp))

            if (response.success) {
                Result.success(true)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetPassword(email: String, newPassword: String): Result<Boolean> {
        return try {
            val response = apiService.resetPassword(
                ResetPasswordRequest(email, newPassword)
            )

            if (response.success) {
                // Update local database if user exists
                val user = userDao.getUserByEmail(email)
                if (user != null) {
                    userDao.updateUser(user.copy(password = newPassword))
                }
                Result.success(true)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserProfile(userEntity: UserEntity): Result<UserEntity> {
        return try {
            userDao.updateUser(userEntity)
            Result.success(userEntity)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}