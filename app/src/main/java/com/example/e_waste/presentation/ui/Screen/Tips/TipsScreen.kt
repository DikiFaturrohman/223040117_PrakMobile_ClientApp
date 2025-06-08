package com.example.e_waste.presentation.ui.Screen.Tips

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.e_waste.domain.model.Tip
import com.example.e_waste.presentation.ui.viewmodels.TipViewModel

@Composable
fun TipsScreen(
    viewModel: TipViewModel = hiltViewModel()
) {
    // Mengambil state dari ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Box digunakan untuk mengatur posisi loading atau pesan error di tengah layar
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Beri padding di sini agar konten tidak menempel ke tepi
        contentAlignment = Alignment.Center
    ) {
        when {
            // Saat data sedang dimuat
            uiState.isLoading -> {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
            // Saat terjadi error
            uiState.error != null -> {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
            // Saat data berhasil dimuat
            else -> {
                // LazyColumn untuk menampilkan daftar tips yang bisa di-scroll
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            "Tips & Trik",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    items(uiState.tips) { tip ->
                        TipItem(tip = tip)
                    }
                }
            }
        }
    }
}

/**
 * Composable untuk menampilkan satu item Tip.
 * Menggunakan Column dengan border untuk gaya minimalis.
 */
@Composable
private fun TipItem(tip: Tip) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Text(
            text = tip.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = tip.content,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}