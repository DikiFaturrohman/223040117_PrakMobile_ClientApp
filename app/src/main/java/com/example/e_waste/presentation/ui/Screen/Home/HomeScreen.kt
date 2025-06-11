package com.example.e_waste.presentation.ui.Screen.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.MonetizationOn // Import ikon dolar/koin baru
import androidx.compose.material.icons.filled.LocalShipping // Untuk waste & get point
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Headphones // Untuk customer service
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.e_waste.data.entity.EWasteEntity
import com.example.e_waste.presentation.ui.theme.EWasteGreenBackground
import com.example.e_waste.presentation.ui.theme.EWasteGreenPrimary
import com.example.e_waste.presentation.ui.theme.EWasteDarkBlue
import com.example.e_waste.presentation.ui.theme.EWasteYellow
import com.example.e_waste.presentation.ui.viewmodels.AuthViewModel
import com.example.e_waste.presentation.ui.viewmodels.EWasteViewModel
import com.example.e_waste.presentation.ui.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    eWasteViewModel: EWasteViewModel = hiltViewModel(),
    onLogout: () -> Unit,
    // >>> TAMBAHKAN PARAMETER INI <<<
    onNavigateToCategorySelection: () -> Unit
) {
    val eWastes by eWasteViewModel.eWastes.collectAsStateWithLifecycle()
    val profileState by profileViewModel.profileState.collectAsStateWithLifecycle()

    // Untuk mengatasi "Unresolved reference 'fillParentMaxSize'" dari pertanyaan sebelumnya
    val eWasteState by eWasteViewModel.eWasteState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        profileViewModel.loadUserProfile()
        eWasteViewModel.refreshDataFromApi()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Header Section
            item {
                HomeHeader(
                    userName = profileState.user?.name ?: "Nama Pengguna",
                    onNotificationClick = { /* TODO: Handle notification click */ }
                )
            }

            item {
                ElectronicPointCard(
                    electronicPoints = 10000,
                    onWithdrawClick = { /* TODO: Handle withdraw click */ }
                )
            }

            // Our Services Section
            item {
                Spacer(modifier = Modifier.height(18.dp)) // Memberi jarak dari card poin ke layanan
                OurServicesSection(
                    // >>> TERUSKAN LAMBDA NAVIGASI KE OurServicesSection <<<
                    onNavigateToCategorySelection = onNavigateToCategorySelection
                )
            }

            // E-Waste Information Section
            item {
                Spacer(modifier = Modifier.height(24.dp)) // Memberi jarak dari layanan ke judul informasi e-waste
                Text(
                    "Informasi E-Waste",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }

            // Bagian untuk menampilkan data e-waste atau pesan kosong
            if (eWastes.isEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp)) // Jarak jika tidak ada data
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { // Diperbaiki dari fillParentMaxSize()
                        Text(
                            "Tidak ada data e-waste saat ini${if (eWasteState.selectedCategory != null) " untuk kategori '${eWasteState.selectedCategory}'" else ""}.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(eWastes) { ewaste ->
                    Spacer(modifier = Modifier.height(12.dp)) // Jarak antar EWasteItem
                    EWasteItem(ewaste)
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp)) // Jarak di bagian bawah daftar e-waste
                }
            }
        }
    }
}

@Composable
fun HomeHeader(
    userName: String,
    onNotificationClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(EWasteGreenBackground),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Bagian Logo E-Waste Management (ikon di atas teks)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Recycling,
                    contentDescription = "E-Waste Logo",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    "E-Waste",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = Color.White
                )
                Text(
                    "MANAGEMENT",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp
                    ),
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Halo, $userName!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                IconButton(onClick = onNotificationClick) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ElectronicPointCard(
    electronicPoints: Int,
    onWithdrawClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y= -24.dp)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = EWasteDarkBlue),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MonetizationOn,
                    contentDescription = "Electronic Point",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Electronic Point:", style = MaterialTheme.typography.bodySmall, color = Color.White)
                    Text(
                        "${electronicPoints}",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = EWasteYellow
                    )
                }
            }
            Button(
                onClick = onWithdrawClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = EWasteDarkBlue
                )
            ) {
                Text("Withdraw")
            }
        }
    }
}

@Composable
fun OurServicesSection(
    onNavigateToCategorySelection: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            "Our Services",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(16.dp)) // Memberi jarak antara judul dan item layanan
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ServiceItem(
                icon = Icons.Default.LocalShipping,
                label = "Pick a Waste",
                onClick = onNavigateToCategorySelection
            )
            ServiceItem(
                icon = Icons.Default.History,
                label = "My History",
                onClick = { /* TODO: Handle History click */ }
            )
            ServiceItem(
                icon = Icons.Default.Headphones,
                label = "Customer Service",
                onClick = { /* TODO: Handle Customer Service click */ }
            )
        }
    }
}

@Composable
fun ServiceItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Surface(
            modifier = Modifier.size(60.dp),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            contentColor = EWasteGreenPrimary
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.padding(12.dp),
                tint = EWasteGreenPrimary
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EWasteItem(eWaste: EWasteEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = eWaste.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = eWaste.category,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = eWaste.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}