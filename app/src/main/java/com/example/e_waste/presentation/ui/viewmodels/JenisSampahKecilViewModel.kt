package com.example.e_waste.presentation.ui.viewmodels

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn // Penting untuk Flow ke StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


data class SmallWasteItem(
    val id: String,
    val name: String,
    val icon: ImageVector
)

@HiltViewModel
class JenisSampahKecilViewModel @Inject constructor() : ViewModel() {

    // Map item ID ke kuantitasnya
    private val _itemQuantities = MutableStateFlow<Map<String, Int>>(emptyMap())
    val itemQuantities: StateFlow<Map<String, Int>> = _itemQuantities.asStateFlow()

    // State turunan: cek apakah ada item yang memiliki kuantitas > 0
    val hasSelectedItems: StateFlow<Boolean> = _itemQuantities.map { quantities ->
        quantities.any { it.value > 0 }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000), // Akan aktif selama ada observer (timeout 5 detik)
        false // Nilai awal
    )

    fun incrementQuantity(itemId: String) {
        _itemQuantities.update { currentMap ->
            val currentQuantity = currentMap.getOrElse(itemId) { 0 }
            currentMap + (itemId to currentQuantity + 1)
        }
    }

    fun decrementQuantity(itemId: String) {
        _itemQuantities.update { currentMap ->
            val currentQuantity = currentMap.getOrElse(itemId) { 0 }
            if (currentQuantity > 0) {
                currentMap + (itemId to currentQuantity - 1)
            } else {
                currentMap // Kuantitas tidak bisa di bawah nol
            }
        }
    }

    fun getQuantity(itemId: String): Int {
        return _itemQuantities.value.getOrElse(itemId) { 0 }
    }

    // Ini bisa digunakan untuk mendapatkan daftar item yang dipilih beserta kuantitasnya
    val selectedItemsWithQuantities: StateFlow<List<Pair<String, Int>>> = _itemQuantities.map { quantities ->
        quantities.filter { it.value > 0 }.toList()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000), // Akan aktif selama ada observer (timeout 5 detik)
        emptyList() // Nilai awal
    )
}