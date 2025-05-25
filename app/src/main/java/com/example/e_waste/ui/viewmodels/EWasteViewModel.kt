package com.example.e_waste.ui.viewmodels


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
