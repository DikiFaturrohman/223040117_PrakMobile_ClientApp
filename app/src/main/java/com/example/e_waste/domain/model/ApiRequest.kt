package com.example.e_waste.domain.model


// Request Classes
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val phone: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class OtpRequest(
    val email: String
)

data class VerifyOtpRequest(
    val email: String,
    val otp: String
)

data class ResetPasswordRequest(
    val email: String,
    val newPassword: String
)