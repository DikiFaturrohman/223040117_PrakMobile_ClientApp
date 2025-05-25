package com.example.e_waste.data.api

import com.example.e_waste.domain.model.ApiResponse
import com.example.e_waste.domain.model.CategoryResponse
import com.example.e_waste.domain.model.EWasteResponse
import com.example.e_waste.domain.model.LoginRequest
import com.example.e_waste.domain.model.OtpRequest
import com.example.e_waste.domain.model.OtpResponse
import com.example.e_waste.domain.model.RegisterRequest
import com.example.e_waste.domain.model.ResetPasswordRequest
import com.example.e_waste.domain.model.ResetPasswordResponse
import com.example.e_waste.domain.model.TokenResponse
import com.example.e_waste.domain.model.UserResponse
import com.example.e_waste.domain.model.VerifyOtpRequest
import com.example.e_waste.domain.model.VerifyOtpResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): ApiResponse<UserResponse>

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): ApiResponse<TokenResponse>

    @POST("auth/send-otp")
    suspend fun sendOtp(@Body otpRequest: OtpRequest): ApiResponse<OtpResponse>

    @POST("auth/verify-otp")
    suspend fun verifyOtp(@Body verifyOtpRequest: VerifyOtpRequest): ApiResponse<VerifyOtpResponse>

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest): ApiResponse<ResetPasswordResponse>

    @GET("ewaste")
    suspend fun getEWastes(): ApiResponse<List<EWasteResponse>>

    @GET("ewaste/categories")
    suspend fun getEWasteCategories(): ApiResponse<List<CategoryResponse>>
}