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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EWasteState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedCategory: String? = null // Add this to hold the selected category
)

@HiltViewModel
class EWasteViewModel @Inject constructor(
    private val eWasteRepository: EWasteRepository
) : ViewModel() {

    private val _eWasteState = MutableStateFlow(EWasteState())
    val eWasteState: StateFlow<EWasteState> = _eWasteState.asStateFlow()

    // A MutableStateFlow to hold the currently selected category for filtering
    private val _selectedCategoryFilter = MutableStateFlow<String?>(null)

    // This flow will now react to changes in _selectedCategoryFilter
    val eWastes: StateFlow<List<EWasteEntity>> = combine(
        _selectedCategoryFilter,
        eWasteRepository.getAllEWastes() // Always observe all e-wastes from DB
    ) { selectedCategory, allEWastes ->
        if (selectedCategory.isNullOrBlank() || selectedCategory == "All") {
            allEWastes // No filter applied, return all
        } else {
            allEWastes.filter { it.category == selectedCategory } // Filter by category
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        refreshDataFromApi()
    }

    fun refreshDataFromApi() {
        viewModelScope.launch {
            _eWasteState.update { it.copy(isLoading = true, error = null) }
            try {
                eWasteRepository.refreshEWastes()
                _eWasteState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _eWasteState.update { it.copy(isLoading = false, error = "Gagal mengambil data dari server: ${e.message}") }
            }
        }
    }

    fun setCategoryFilter(category: String?) {
        _selectedCategoryFilter.value = category
        _eWasteState.update { it.copy(selectedCategory = category) }
    }

    fun clearError() {
        _eWasteState.update { it.copy(error = null) }
    }
}