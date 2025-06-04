package com.example.e_waste.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_waste.data.entity.UserEntity
import com.example.e_waste.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val registrationSuccess: Boolean = false,
    val loginSuccess: Boolean = false,
    val otpSent: Boolean = false,
    val otpVerified: Boolean = false,
    val passwordResetSuccess: Boolean = false,
    val loggedInUserEmail: String? = null // To hold email for OTP flows
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun resetAuthState() {
        _authState.value = AuthState(loggedInUserEmail = _authState.value.loggedInUserEmail) // Keep email if needed for next step
    }

    fun clearError() {
        _authState.update { it.copy(error = null) }
    }

    fun register(name: String, email: String, phone: String, password: String) {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, error = null) }
            val result = userRepository.registerUser(name, email, password, phone)
            result.fold(
                onSuccess = {
                    _authState.update {
                        it.copy(
                            isLoading = false,
                            registrationSuccess = true,
                            loggedInUserEmail = email // Store email for OTP verification
                        )
                    }
                },
                onFailure = { e ->
                    _authState.update { it.copy(isLoading = false, error = e.message ?: "Registration failed") }
                }
            )
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, error = null) }
            val result = userRepository.login(email, password)
            result.fold(
                onSuccess = { token -> // Assuming token is success indicator for now
                    // Here you would typically save the token
                    _authState.update { it.copy(isLoading = false, loginSuccess = true, loggedInUserEmail = email) }
                },
                onFailure = { e ->
                    _authState.update { it.copy(isLoading = false, error = e.message ?: "Login failed") }
                }
            )
        }
    }

    fun sendOtp(email: String) {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, error = null) }
            val result = userRepository.sendOtp(email)
            result.fold(
                onSuccess = {
                    _authState.update { it.copy(isLoading = false, otpSent = true, loggedInUserEmail = email) }
                },
                onFailure = { e ->
                    _authState.update { it.copy(isLoading = false, error = e.message ?: "Failed to send OTP") }
                }
            )
        }
    }

    fun verifyOtp(email: String, otp: String) {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, error = null) }
            val result = userRepository.verifyOtp(email, otp)
            result.fold(
                onSuccess = {
                    _authState.update { it.copy(isLoading = false, otpVerified = true) }
                },
                onFailure = { e ->
                    _authState.update { it.copy(isLoading = false, error = e.message ?: "OTP verification failed") }
                }
            )
        }
    }

    fun resetPassword(email: String, newPassword: String) {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, error = null) }
            val result = userRepository.resetPassword(email, newPassword)
            result.fold(
                onSuccess = {
                    _authState.update { it.copy(isLoading = false, passwordResetSuccess = true) }
                },
                onFailure = { e ->
                    _authState.update { it.copy(isLoading = false, error = e.message ?: "Password reset failed") }
                }
            )
        }
    }
}