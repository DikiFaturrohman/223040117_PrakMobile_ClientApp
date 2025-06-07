package com.example.e_waste.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_waste.data.repository.TipRepository
import com.example.e_waste.domain.model.Tip
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// State untuk UI, berisi daftar tips, status loading, dan pesan error
data class TipsUiState(
    val tips: List<Tip> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class TipViewModel @Inject constructor(
    private val tipRepository: TipRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TipsUiState())
    val uiState: StateFlow<TipsUiState> = _uiState.asStateFlow()

    init {
        // Langsung panggil fungsi untuk mengambil data saat ViewModel dibuat
        fetchTips()
    }

    fun fetchTips() {
        viewModelScope.launch {
            // Set state ke loading sebelum memanggil API
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = tipRepository.getTips()
            result.fold(
                onSuccess = { tipsList ->
                    // Jika sukses, update state dengan daftar tips
                    _uiState.update {
                        it.copy(isLoading = false, tips = tipsList)
                    }
                },
                onFailure = { error ->
                    // Jika gagal, update state dengan pesan error
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message ?: "Gagal memuat data")
                    }
                }
            )
        }
    }
}