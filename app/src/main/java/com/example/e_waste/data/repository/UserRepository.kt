package com.example.e_waste.data.repository

import com.example.e_waste.data.api.ApiService
import com.example.e_waste.data.dao.UserDao
import com.example.e_waste.data.entity.UserEntity
import com.example.e_waste.domain.model.LoginRequest
import com.example.e_waste.domain.model.RegisterRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager,
    private val userDao: UserDao
) {
    suspend fun register(registerRequest: RegisterRequest): Result<Unit> {
        return try {
            val response = apiService.register(registerRequest)
            if (response.success && response.data != null) {
                val user = response.data
                userDao.insertUser(UserEntity(id = user.id.toString(), email = user.email, name = user.name, phone = user.phone, password = ""))
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(loginRequest: LoginRequest): Result<Unit> {
        return try {
            val response = apiService.login(loginRequest)
            if (response.success && response.data != null) {
                // Simpan token DAN email ke SessionManager
                sessionManager.saveToken(response.data)
                sessionManager.saveEmail(loginRequest.email)
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Unit> {
        return try {
            apiService.logout()
            sessionManager.clearToken()
            Result.success(Unit)
        } catch (e: Exception) {
            sessionManager.clearToken()
            Result.failure(e)
        }
    }

    fun getUserByEmailFlow(email: String): Flow<UserEntity?> {
        return userDao.getUserByEmailFlow(email)
    }

    suspend fun refreshUserProfile(): Result<Unit> {
        return try {
            val response = apiService.getProfile()
            if (response.success && response.data != null) {
                val user = response.data
                userDao.insertUser(UserEntity(id = user.id.toString(), email = user.email, name = user.name, phone = user.phone, password = ""))
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserProfile(userEntity: UserEntity): Result<UserEntity> {
        userDao.updateUser(userEntity)
        // TODO: Idealnya panggil juga API untuk update profil di server
        return Result.success(userEntity)
    }
}