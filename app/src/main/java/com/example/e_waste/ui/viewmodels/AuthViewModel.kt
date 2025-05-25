package com.example.e_waste.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_waste.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// AuthViewModel.kt
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState = _registerState.asStateFlow()

    private val _otpState = MutableStateFlow<OtpState>(OtpState.Idle)
    val otpState = _otpState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = AuthState.Loading

            val result = userRepository.login(email, password)
            _loginState.value = result.fold(
                onSuccess = { AuthState.Success(it) },
                onFailure = { AuthState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun register(name: String, email: String, password: String, phone: String) {
        viewModelScope.launch {
            _registerState.value = AuthState.Loading

            val result = userRepository.registerUser(name, email, password, phone)
            _registerState.value = result.fold(
                onSuccess = { AuthState.Success("Registration successful") },
                onFailure = { AuthState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun sendOtp(email: String) {
        viewModelScope.launch {
            _otpState.value = OtpState.Loading

            val result = userRepository.sendOtp(email)
            _otpState.value = result.fold(
                onSuccess = { OtpState.OtpSent },
                onFailure = { OtpState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun verifyOtp(email: String, otp: String) {
        viewModelScope.launch {
            _otpState.value = OtpState.Loading

            val result = userRepository.verifyOtp(email, otp)
            _otpState.value = result.fold(
                onSuccess = { OtpState.OtpVerified },
                onFailure = { OtpState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun resetPassword(email: String, newPassword: String) {
        viewModelScope.launch {
            _loginState.value = AuthState.Loading

            val result = userRepository.resetPassword(email, newPassword)
            _loginState.value = result.fold(
                onSuccess = { AuthState.Success("Password reset successful") },
                onFailure = { AuthState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val data: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }

    sealed class OtpState {
        object Idle : OtpState()
        object Loading : OtpState()
        object OtpSent : OtpState()
        object OtpVerified : OtpState()
        data class Error(val message: String) : OtpState()
    }
}