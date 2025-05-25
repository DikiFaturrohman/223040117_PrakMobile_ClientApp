package com.example.e_waste.data.dao

// UserDao.kt
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun validateUser(email: String, password: String): UserEntity?

    @Update
    suspend fun updateUser(user: UserEntity)
}