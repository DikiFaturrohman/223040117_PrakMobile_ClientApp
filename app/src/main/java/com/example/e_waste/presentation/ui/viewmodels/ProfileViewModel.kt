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

data class ProfileState(
    val user: UserEntity? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val updateSuccess: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    // This email would typically come from a session manager or arguments
    // For simplicity, we might need to pass it or have a placeholder.
    fun loadUserProfile(email: String) {
        viewModelScope.launch {
            _profileState.update { it.copy(isLoading = true, error = null) }
            try {
                val user = userRepository.getUserByEmail(email) // Assumes this comes from local DB
                if (user != null) {
                    _profileState.update { it.copy(isLoading = false, user = user) }
                } else {
                    _profileState.update { it.copy(isLoading = false, error = "User not found") }
                }
            } catch (e: Exception) {
                _profileState.update { it.copy(isLoading = false, error = e.message ?: "Failed to load profile") }
            }
        }
    }

    fun updateUserProfile(name: String, phone: String, currentEmail: String) {
        viewModelScope.launch {
            _profileState.update { it.copy(isLoading = true, error = null, updateSuccess = false) }
            val currentUser = _profileState.value.user
            if (currentUser == null || currentUser.email != currentEmail) {
                _profileState.update { it.copy(isLoading = false, error = "User data not loaded or mismatch.") }
                return@launch
            }

            // Create an updated UserEntity. Password and email are not changed here.
            // ID and createdAt should be preserved from the original currentUser object.
            val updatedUser = currentUser.copy(
                name = name,
                phone = phone
            )

            val result = userRepository.updateUserProfile(updatedUser)
            result.fold(
                onSuccess = { savedUser ->
                    _profileState.update { it.copy(isLoading = false, updateSuccess = true, user = savedUser) }
                },
                onFailure = { e ->
                    _profileState.update { it.copy(isLoading = false, error = e.message ?: "Profile update failed") }
                }
            )
        }
    }

    fun resetUpdateStatus() {
        _profileState.update { it.copy(updateSuccess = false, error = null) }
    }

    fun clearError() {
        _profileState.update { it.copy(error = null) }
    }
}