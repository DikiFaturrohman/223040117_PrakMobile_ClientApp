package com.example.e_waste.data.api

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