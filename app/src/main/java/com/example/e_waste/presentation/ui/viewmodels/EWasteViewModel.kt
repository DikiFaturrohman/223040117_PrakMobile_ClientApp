package com.example.e_waste.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_waste.data.entity.EWasteEntity
import com.example.e_waste.data.repository.EWasteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Kembalikan data class EWasteState
data class EWasteState(
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class EWasteViewModel @Inject constructor(
    private val eWasteRepository: EWasteRepository
) : ViewModel() {

    // Definisikan kembali _eWasteState
    private val _eWasteState = MutableStateFlow(EWasteState())
    val eWasteState: StateFlow<EWasteState> = _eWasteState.asStateFlow()

    // Flow ini akan selalu mengambil data dari database lokal (cache)
    val eWastes: StateFlow<List<EWasteEntity>> = eWasteRepository.getAllEWastes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Blok init akan dieksekusi saat ViewModel dibuat
    init {
        refreshDataFromApi()
    }

    private fun refreshDataFromApi() {
        viewModelScope.launch {
            _eWasteState.update { it.copy(isLoading = true, error = null) }
            try {
                eWasteRepository.refreshEWastes()
                _eWasteState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _eWasteState.update { it.copy(isLoading = false, error = "Gagal mengambil data dari server.") }
            }
        }
    }

    // Tambahkan kembali fungsi clearError()
    fun clearError() {
        _eWasteState.update { it.copy(error = null) }
    }
}