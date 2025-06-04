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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EWasteState(
    val eWastes: List<EWasteEntity> = emptyList(),
    val categories: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedCategory: String? = null
)

@HiltViewModel
class EWasteViewModel @Inject constructor(
    private val eWasteRepository: EWasteRepository
) : ViewModel() {

    private val _eWasteState = MutableStateFlow(EWasteState())
    val eWasteState: StateFlow<EWasteState> = _eWasteState.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)

    val eWastes: StateFlow<List<EWasteEntity>> = _selectedCategory.flatMapLatest { category ->
        if (category == null || category == "All") {
            eWasteRepository.getAllEWastes()
        } else {
            eWasteRepository.getEWastesByCategory(category)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        loadCategories()
        refreshEWastesData()
    }

    fun refreshEWastesData() {
        viewModelScope.launch {
            _eWasteState.update { it.copy(isLoading = true, error = null) }
            try {
                eWasteRepository.refreshEWastes() // Fetches from API and saves to DB
                // Data is observed via the Flow from DAO, so no explicit update here is needed for eWastes list
                _eWasteState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _eWasteState.update { it.copy(isLoading = false, error = e.message ?: "Failed to refresh e-waste data") }
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            _eWasteState.update { it.copy(isLoading = true, error = null) }
            val result = eWasteRepository.getEWasteCategories()
            result.fold(
                onSuccess = { categoryNames ->
                    _eWasteState.update { it.copy(isLoading = false, categories = listOf("All") + categoryNames) }
                },
                onFailure = { e ->
                    _eWasteState.update { it.copy(isLoading = false, error = e.message ?: "Failed to load categories") }
                }
            )
        }
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = if (category == "All") null else category
        _eWasteState.update { it.copy(selectedCategory = category) }
    }

    fun clearError() {
        _eWasteState.update { it.copy(error = null) }
    }
}