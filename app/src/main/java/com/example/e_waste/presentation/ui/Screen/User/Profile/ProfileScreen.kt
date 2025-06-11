package com.example.e_waste.presentation.ui.Screen.User.Profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.e_waste.presentation.ui.theme.EWasteGreenPrimary
import com.example.e_waste.presentation.ui.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit // Parameter onLogout tetap dipertahankan untuk tombol logout di TopAppBar
) {
    val profileState by profileViewModel.profileState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        profileViewModel.loadUserProfile()
    }

    val user = profileState.user

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Profile",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = EWasteGreenPrimary)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Top Section (Green background with profile info)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(EWasteGreenPrimary),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Profile Picture
                AsyncImage(
                    model = "https://via.placeholder.com/150", // Placeholder image
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))

                // User Name (from ProfileViewModel)
                Text(
                    text = user?.name ?: "Nama Pengguna",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                // User Email (from ProfileViewModel)
                Text(
                    text = user?.email ?: "email@example.com",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Card untuk menampilkan informasi profil (tanpa edit)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    // Nama Lengkap
                    ProfileInfoDisplay("Nama Lengkap", user?.name ?: "Belum diatur")
                    Spacer(modifier = Modifier.height(16.dp))

                    // Alamat Email
                    ProfileInfoDisplay("Alamat Email", user?.email ?: "Belum diatur")
                    Spacer(modifier = Modifier.height(16.dp))

                    // No Handphone
                    ProfileInfoDisplay("No Handphone", user?.phone ?: "Belum diatur")

                    // Catatan: Tombol "Ubah Profile" dari desain Edit Profile tidak disertakan di sini
                    // karena ini adalah halaman tampilan profil saja.
                    // Jika Anda ingin tombol "Ubah Profile" di sini untuk menavigasi ke EditProfileScreen,
                    // Anda harus menambahkannya secara eksplisit dan meneruskan onNavigateToEditProfile.
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Composable helper untuk menampilkan satu baris informasi profil.
 */
@Composable
private fun ProfileInfoDisplay(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant // Warna abu-abu untuk label
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
        )
        // Garis pemisah bawah, mirip dengan OutlinedTextField
        Divider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f) // Warna garis tipis
        )
    }
}