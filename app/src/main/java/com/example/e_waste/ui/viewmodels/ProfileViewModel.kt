package com.example.e_waste.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_waste.data.entity.UserEntity
import com.example.e_waste.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ProfileViewModel.kt
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _updateProfileState = MutableStateFlow<UpdateProfileState>(UpdateProfileState.Idle)
    val updateProfileState = _updateProfileState.asStateFlow()

    fun updateProfile(user: UserEntity) {
        viewModelScope.launch {
            _updateProfileState.value = UpdateProfileState.Loading

            val result = userRepository.updateUserProfile(user)
            _updateProfileState.value = result.fold(
                onSuccess = { UpdateProfileState.Success(it) },
                onFailure = { UpdateProfileState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    sealed class UpdateProfileState {
        object Idle : UpdateProfileState()
        object Loading : UpdateProfileState()
        data class Success(val user: UserEntity) : UpdateProfileState()
        data class Error(val message: String) : UpdateProfileState()
    }
}