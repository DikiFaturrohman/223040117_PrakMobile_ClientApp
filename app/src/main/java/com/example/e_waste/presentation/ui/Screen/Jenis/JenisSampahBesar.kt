package com.example.e_waste.presentation.ui.Screen.JenisSampahBesar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
// Impor ikon untuk jenis sampah besar
import androidx.compose.material.icons.filled.LocalLaundryService // Mesin Cuci
import androidx.compose.material.icons.filled.Kitchen // Kulkas
import androidx.compose.material.icons.filled.Tv // TV
import androidx.compose.material.icons.filled.Print // <<< UBAH DARI .Printer MENJADI .Print >>>
import androidx.compose.material.icons.filled.LocalShipping // Untuk ikon "Request a Pickup"
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.example.e_waste.presentation.ui.theme.EWasteGreenPrimary
import com.example.e_waste.presentation.ui.theme.EwasteTheme
import com.example.e_waste.presentation.ui.viewmodels.JenisSampahBesarViewModel // Import ViewModel baru
import com.example.e_waste.presentation.ui.viewmodels.LargeWasteItem // Import LargeWasteItem dari ViewModel

// Daftar jenis sampah besar yang baru sesuai UI Anda
val largeWasteTypes = listOf(
    LargeWasteItem("mesin_cuci", "Mesin Cuci", Icons.Default.LocalLaundryService),
    LargeWasteItem("kulkas", "Kulkas", Icons.Default.Kitchen),
    LargeWasteItem("tv", "TV", Icons.Default.Tv),
    LargeWasteItem("printer", "Printer", Icons.Default.Print) // <<< UBAH DARI .Printer MENJADI .Print >>>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JenisSampahBesarScreen(
    onNavigateBack: () -> Unit,
    // onContinue: (List<Pair<String, Int>>) -> Unit,
    viewModel: JenisSampahBesarViewModel = hiltViewModel()
) {
    val itemQuantities by viewModel.itemQuantities.collectAsStateWithLifecycle()
    val hasSelectedItems by viewModel.hasSelectedItems.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Jenis Sampah Besar",
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
        },
        bottomBar = {
            if (hasSelectedItems) {
                BottomAppBar(
                    containerColor = EWasteGreenPrimary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { /* onContinue(viewModel.selectedItemsWithQuantities.value) */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = EWasteGreenPrimary
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.LocalShipping, contentDescription = "Request Pickup Icon", modifier = Modifier.size(24.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Request a Pickup",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    "Pilih jenis sampah elektronik besar dan tentukan jumlahnya:",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(largeWasteTypes) { item ->
                val quantity = itemQuantities.getOrElse(item.id) { 0 }
                LargeWasteTypeCard(
                    item = item,
                    quantity = quantity,
                    onIncrement = { viewModel.incrementQuantity(item.id) },
                    onDecrement = { viewModel.decrementQuantity(item.id) }
                )
            }
        }
    }
}

@Composable
fun LargeWasteTypeCard(
    item: LargeWasteItem,
    quantity: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Bagian kiri: Ikon dan Nama Item
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.name,
                    tint = EWasteGreenPrimary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Bagian kanan: Kontrol Kuantitas
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Tombol Kurang (-)
                IconButton(
                    onClick = onDecrement,
                    enabled = quantity > 0
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Kurangi",
                        tint = if (quantity > 0) EWasteGreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Tampilan Kuantitas
                Text(
                    text = "$quantity",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.width(40.dp),
                    textAlign = TextAlign.Center
                )

                // Tombol Tambah (+)
                IconButton(onClick = onIncrement) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tambah",
                        tint = EWasteGreenPrimary
                    )
                }
            }
        }
    }
}


