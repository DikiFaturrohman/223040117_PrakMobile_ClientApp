package com.example.e_waste.ui.viewmodels

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