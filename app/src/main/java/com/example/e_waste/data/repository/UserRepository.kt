package com.example.e_waste.data.repository

import com.example.e_waste.data.api.ApiService
import com.example.e_waste.data.dao.UserDao
import com.example.e_waste.data.entity.UserEntity
import com.example.e_waste.domain.model.LoginRequest
import com.example.e_waste.domain.model.RegisterRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager,
    private val userDao: UserDao // Masih kita gunakan untuk cache profil
) {
    suspend fun register(registerRequest: RegisterRequest): Result<Unit> {
        return try {
            val response = apiService.register(registerRequest)
            if (response.success && response.data != null) {
                // Simpan data user ke database lokal sebagai cache
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
                // Simpan token ke SessionManager
                sessionManager.saveToken(response.data)
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
            apiService.logout() // Panggil API logout
            sessionManager.clearToken() // Hapus token dari session
            Result.success(Unit)
        } catch (e: Exception) {
            // Bahkan jika API gagal, tetap hapus token lokal
            sessionManager.clearToken()
            Result.failure(e)
        }
    }

    suspend fun getUserByEmail(email: String): UserEntity? {
        // Fungsi ini sekarang hanya mengambil dari cache lokal
        return userDao.getUserByEmail(email)
    }

    suspend fun updateUserProfile(userEntity: UserEntity): Result<UserEntity> {
        // TODO: Buat endpoint API untuk update profil di Laravel & panggil di sini
        // Untuk sekarang, kita hanya update di lokal
        userDao.updateUser(userEntity)
        return Result.success(userEntity)
    }
}