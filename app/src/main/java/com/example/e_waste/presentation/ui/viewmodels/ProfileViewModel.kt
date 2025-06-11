// app/src/main/java/com/example/e_waste/presentation/ui/viewmodels/ProfileViewModel.kt
package com.example.e_waste.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_waste.data.entity.UserEntity
import com.example.e_waste.data.repository.SessionManager
import com.example.e_waste.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileState(
    val user: UserEntity? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val updateSuccess: Boolean = false // Tetap dipertahankan untuk kemungkinan feedback dari layar lain
    // nameInput, phoneInput, passwordInput dihapus karena tidak lagi di layar ini
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager // Inject SessionManager
) : ViewModel() {

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        val userEmail = sessionManager.getEmail()
        if (userEmail == null) {
            _profileState.update { it.copy(error = "Sesi tidak ditemukan, silakan login ulang.") }
            return
        }

        viewModelScope.launch {
            userRepository.getUserByEmailFlow(userEmail).collect { userFromDb ->
                _profileState.update { currentState ->
                    currentState.copy(
                        user = userFromDb,
                        // nameInput, phoneInput, passwordInput tidak lagi diupdate dari sini
                    )
                }
            }
        }

        viewModelScope.launch {
            _profileState.update { it.copy(isLoading = true) }
            val refreshResult = userRepository.refreshUserProfile()
            refreshResult.onFailure { e ->
                _profileState.update { it.copy(error = e.message, isLoading = false) }
            }
            refreshResult.onSuccess {
                _profileState.update { it.copy(isLoading = false) }
            }
        }
    }

    // Fungsi setter untuk input di layar editor (tetap dipertahankan untuk kemungkinan layar lain)
    fun setNameInput(name: String) { /* tidak digunakan langsung di layar ini */ }
    fun setPhoneInput(phone: String) { /* tidak digunakan langsung di layar ini */ }
    fun setPasswordInput(password: String) { /* tidak digunakan langsung di layar ini */ }


    fun updateUserProfile(name: String, phone: String /*, password: String? = null */) {
        viewModelScope.launch {
            val currentUser = _profileState.value.user ?: return@launch
            _profileState.update { it.copy(isLoading = true, error = null, updateSuccess = false) }

            val updatedUser = currentUser.copy(name = name, phone = phone)

            val result = userRepository.updateUserProfile(updatedUser)

            result.fold(
                onSuccess = { savedUser ->
                    _profileState.update { it.copy(isLoading = false, updateSuccess = true, user = savedUser) }
                },
                onFailure = { e ->
                    _profileState.update { it.copy(isLoading = false, error = e.message ?: "Update profil gagal") }
                }
            )
        }
    }

    fun clearError() {
        _profileState.update { it.copy(error = null) }
    }

    fun resetUpdateStatus() {
        _profileState.update { it.copy(updateSuccess = false) }
    }
}