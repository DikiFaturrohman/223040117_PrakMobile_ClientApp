package com.example.e_waste.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_waste.data.repository.EWasteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

// EWasteViewModel.kt
@HiltViewModel
class EWasteViewModel @Inject constructor(
    private val eWasteRepository: EWasteRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories = _categories.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    val eWastes = _selectedCategory.flatMapLatest { category ->
        if (category == null) {
            eWasteRepository.getAllEWastes()
        } else {
            eWasteRepository.getEWastesByCategory(category)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        loadCategories()
        refreshEWastes()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            eWasteRepository.getEWasteCategories().fold(
                onSuccess = { _categories.value = it },
                onFailure = { /* Handle error */ }
            )
        }
    }

    fun refreshEWastes() {
        viewModelScope.launch {
            eWasteRepository.refreshEWastes()
        }
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
    }
}
