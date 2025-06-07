package com.example.e_waste.presentation.ui.Screen.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.e_waste.R
import com.example.e_waste.data.entity.EWasteEntity
import com.example.e_waste.presentation.ui.viewmodels.EWasteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToProfile: () -> Unit,
    onNavigateToTips: () -> Unit,
    onLogout: () -> Unit,
    viewModel: EWasteViewModel = hiltViewModel()
) {
    val eWasteState by viewModel.eWasteState.collectAsStateWithLifecycle()
    val eWastes by viewModel.eWastes.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(eWasteState.error) {
        eWasteState.error?.let {
            snackbarHostState.showSnackbar(message = it)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("E-Waste Manager") },
                actions = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "Profile")
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Welcome Card
            item {
                WelcomeCard(modifier = Modifier.padding(horizontal = 16.dp))
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Quick Access
            item {
                SectionTitle("Akses Cepat", modifier = Modifier.padding(horizontal = 16.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item { QuickAccessButton(Icons.Default.LocalShipping, "Jemput Sampah") }
                    item { QuickAccessButton(Icons.Default.Category, "Kategori") }
                    item { QuickAccessButton(Icons.Default.Lightbulb, "Tips") }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // E-Waste Info Section
            item {
                SectionTitle("Informasi E-Waste", modifier = Modifier.padding(horizontal = 16.dp))
            }

            if (eWasteState.isLoading && eWastes.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            } else if (eWastes.isEmpty()) {
                item {
                    InfoMessage(
                        icon = Icons.Default.Info,
                        message = "Belum ada informasi e-waste yang tersedia."
                    )
                }
            } else {
                items(eWastes) { ewaste ->
                    EWasteInfoCard(
                        eWaste = ewaste,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun WelcomeCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Halo, Pengguna!", style = MaterialTheme.typography.titleMedium)
            Text("Siap untuk mengelola sampah elektronikmu hari ini?", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun QuickAccessButton(icon: ImageVector, label: String, onClick: () -> Unit = {}) {
    OutlinedButton(onClick = onClick) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(label)
    }
}

@Composable
fun EWasteInfoCard(eWaste: EWasteEntity, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = { /* TODO: Navigate to detail */ }
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = eWaste.imageUrl,
                    error = painterResource(id = R.drawable.ic_launcher_background)
                ),
                contentDescription = eWaste.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .weight(0.4f)
                    .aspectRatio(1f)
            )
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .padding(12.dp)
            ) {
                Text(
                    text = eWaste.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = eWaste.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = eWaste.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun InfoMessage(icon: ImageVector, message: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}