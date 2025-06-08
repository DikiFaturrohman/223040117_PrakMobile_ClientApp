package com.example.e_waste.data.api

import com.example.e_waste.domain.model.ApiResponse
import com.example.e_waste.domain.model.EWaste
import com.example.e_waste.domain.model.LoginRequest
import com.example.e_waste.domain.model.RegisterRequest
import com.example.e_waste.domain.model.Tip
import com.example.e_waste.domain.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    suspend fun register(@Body registerRequest: RegisterRequest): ApiResponse<User>

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): ApiResponse<String> // Laravel mengirim token sebagai string di 'data'

    @POST("logout")
    suspend fun logout(): ApiResponse<Any> // Tidak ada data spesifik yang dikembalikan

    @GET("ewaste")
    suspend fun getEWastes(): ApiResponse<List<EWaste>>

    @GET("tips")
    suspend fun getTips(): ApiResponse<List<Tip>>

    @GET("profile") // Endpoint baru
    suspend fun getProfile(): ApiResponse<User>
}