// app/src/main/java/com/example/e_waste/presentation/ui/Screen/KategoriSampah/KategoriSampahScreen.kt
package com.example.e_waste.presentation.ui.Screen.KategoriSampah

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.e_waste.AppDestinations // Pastikan diimpor
import com.example.e_waste.presentation.ui.theme.EWasteGreenPrimary
import com.example.e_waste.presentation.ui.theme.EwasteTheme
import com.example.e_waste.presentation.ui.viewmodels.EWasteViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KategoriSampahScreen(
    onNavigateBack: () -> Unit,
    onNavigateToJenisSampahKecil: () -> Unit,
    onNavigateToJenisSampahBesar: () -> Unit, // <<< PARAMETER BARU UNTUK NAVIGASI KE JENIS SAMPAH BESAR >>>
    eWasteViewModel: EWasteViewModel = hiltViewModel()
) {
    val uiState by eWasteViewModel.eWasteState.collectAsStateWithLifecycle()
    var selectedCategory by remember { mutableStateOf(uiState.selectedCategory) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Kategori Sampah",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Opsi 1: Jenis Sampah Kecil
            CategorySelectionCard(
                categoryName = "Jenis Sampah Kecil",
                isSelected = selectedCategory == "Small",
                onClick = {
                    selectedCategory = "Small"
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Opsi 2: Jenis Sampah Besar
            CategorySelectionCard(
                categoryName = "Jenis Sampah Besar",
                isSelected = selectedCategory == "Large",
                onClick = {
                    selectedCategory = "Large"
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (selectedCategory == "Small") {
                        eWasteViewModel.setCategoryFilter(selectedCategory)
                        onNavigateToJenisSampahKecil()
                    } else if (selectedCategory == "Large") {
                        eWasteViewModel.setCategoryFilter(selectedCategory)
                        onNavigateToJenisSampahBesar() // <<< PANGGIL CALLBACK BARU INI >>>
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EWasteGreenPrimary),
                enabled = selectedCategory != null
            ) {
                Text("Lanjut", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CategorySelectionCard(
    categoryName: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) EWasteGreenPrimary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp, brush = SolidColor(EWasteGreenPrimary))
        else ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(MaterialTheme.colorScheme.outline)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = categoryName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = EWasteGreenPrimary,
                    unselectedColor = MaterialTheme.colorScheme.outline
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewKategoriSampahScreen() {
    EwasteTheme {
        KategoriSampahScreen(onNavigateBack = {}, onNavigateToJenisSampahKecil = {}, onNavigateToJenisSampahBesar = {})
    }
}