package com.example.e_waste.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_waste.data.repository.UserRepository
import com.example.e_waste.domain.model.LoginRequest
import com.example.e_waste.domain.model.RegisterRequest
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
    val loggedInUserEmail: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun resetAuthState() {
        _authState.update { it.copy(isLoading = false, error = null, registrationSuccess = false, loginSuccess = false) }
    }

    fun clearError() {
        _authState.update { it.copy(error = null) }
    }

    fun register(name: String, email: String, phone: String, password: String) {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, error = null) }
            // Buat objek RegisterRequest
            val request = RegisterRequest(name = name, email = email, phone = phone, password = password)
            val result = userRepository.register(request)
            result.fold(
                onSuccess = {
                    _authState.update { it.copy(isLoading = false, registrationSuccess = true) }
                },
                onFailure = { e ->
                    _authState.update { it.copy(isLoading = false, error = e.message ?: "Registrasi gagal") }
                }
            )
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, error = null) }
            // Buat objek LoginRequest
            val request = LoginRequest(email = email, password = password)
            val result = userRepository.login(request)
            result.fold(
                onSuccess = {
                    // Jika login API berhasil, repository sudah menyimpan token.
                    // ViewModel hanya perlu update UI state.
                    _authState.update { it.copy(isLoading = false, loginSuccess = true, loggedInUserEmail = email) }
                },
                onFailure = { e ->
                    _authState.update { it.copy(isLoading = false, error = e.message ?: "Login gagal") }
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            // Panggil fungsi logout di repository
            userRepository.logout()
            // Reset semua state autentikasi
            _authState.value = AuthState()
        }
    }
}