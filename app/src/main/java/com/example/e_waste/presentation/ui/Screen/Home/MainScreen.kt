package com.example.e_waste.presentation.ui.Screen.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource // For drawable resources
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.e_waste.R // Assuming you have a placeholder image in drawable
import com.example.e_waste.data.entity.EWasteEntity
import com.example.e_waste.presentation.ui.theme.EWasteGreen
import com.example.e_waste.presentation.ui.theme.EWasteWhite
import com.example.e_waste.presentation.ui.viewmodels.EWasteViewModel

// Data class for NewsItem (already provided)
data class NewsItem(
    val title: String,
    val date: String,
    val time: String,
    val imageUrl: String // Can be a URL or a drawable resource ID if local
)

// Dummy data for news
val dummyNewsItems = listOf(
    NewsItem("Pentingnya Daur Ulang E-Waste", "04 Jun 2025", "10:00", "https://via.placeholder.com/150/FFC107/000000?Text=News1"),
    NewsItem("Bahaya Limbah Elektronik", "03 Jun 2025", "14:30", "https://via.placeholder.com/150/4CAF50/FFFFFF?Text=News2"),
    NewsItem("Cara Mudah Pilah E-Waste di Rumah", "02 Jun 2025", "09:15", "https://via.placeholder.com/150/E91E63/FFFFFF?Text=News3")
)

// Data class for Quick Access Item
data class QuickAccessItem(
    val icon: ImageVector,
    val label: String,
    val action: () -> Unit = {}
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToProfile: () -> Unit,
    onLogout: () -> Unit, // Callback for logout
    viewModel: EWasteViewModel = hiltViewModel()
) {
    val eWasteState by viewModel.eWasteState.collectAsStateWithLifecycle()
    val eWastes by viewModel.eWastes.collectAsStateWithLifecycle() // Observe the Flow of EWasteEntity
    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current

    val quickAccessItems = listOf(
        QuickAccessItem(Icons.Filled.Category, "Kategori") { /* TODO: Navigate or show categories */ },
        QuickAccessItem(Icons.Filled.LocalShipping, "Jemput E-Waste") { /* TODO: Navigate to pickup */ },
        QuickAccessItem(Icons.Filled.Lightbulb, "Tips & Trik") { /* TODO: Navigate to tips */ },
        QuickAccessItem(Icons.Filled.HeadsetMic, "Customer Service") { /* TODO: Navigate to CS */ }
    )

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
                title = { Text("E-Waste App", color = EWasteWhite) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = EWasteGreen),
                actions = {
                    IconButton(onClick = { /* TODO: Search Action */ }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search", tint = EWasteWhite)
                    }
                    IconButton(onClick = { /* TODO: Notification Action */ }) {
                        Icon(Icons.Filled.Notifications, contentDescription = "Notifications", tint = EWasteWhite)
                    }
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "Profile", tint = EWasteWhite)
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout", tint = EWasteWhite)
                    }
                }
            )
        },
        // FloatingActionButton can be added here if needed
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Gray.copy(alpha = 0.05f))
        ) {
            // Banner Section (Placeholder)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(16.dp)
                        .background(EWasteGreen, RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    // You can use a Pager for multiple banners
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background), // Replace with your banner image
                        contentDescription = "Promotional Banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop // Or ContentScale.Fit
                    )
                    Text(
                        "Kelola E-Waste Anda Sekarang!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = EWasteWhite,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Quick Access Menu
            item {
                QuickAccessMenu(items = quickAccessItems, modifier = Modifier.padding(vertical = 8.dp))
            }

            // Categories Filter
            item {
                if (eWasteState.categories.isNotEmpty()) {
                    Text(
                        "Filter by Category",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(eWasteState.categories) { category ->
                            ChipCategory(
                                categoryName = category,
                                isSelected = category == eWasteState.selectedCategory || (category == "All" && eWasteState.selectedCategory == null),
                                onCategorySelected = { viewModel.selectCategory(it) }
                            )
                        }
                    }
                }
            }


            // Berita Terkini Section
            item {
                SectionTitle("Berita Terkini", modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp))
            }
            items(dummyNewsItems) { newsItem ->
                NewsCard(newsItem = newsItem, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            }

            // E-Waste Items Section
            item {
                SectionTitle("Info E-Waste", modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp))
            }

            if (eWasteState.isLoading && eWastes.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            } else if (eWastes.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                        Text("No e-waste items found for the selected category.")
                    }
                }
            } else {
                items(eWastes) { ewaste ->
                    EWasteItemCard(eWaste = ewaste, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                }
            }
            item { Spacer(modifier = Modifier.height(16.dp)) } // Add some space at the bottom
        }
    }
}

@Composable
fun ChipCategory(categoryName: String, isSelected: Boolean, onCategorySelected: (String) -> Unit) {
    FilterChip(
        selected = isSelected,
        onClick = { onCategorySelected(categoryName) },
        label = { Text(categoryName) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = EWasteGreen,
            selectedLabelColor = EWasteWhite
        ),
        modifier = Modifier.height(36.dp)
    )
}


@Composable
fun QuickAccessMenu(items: List<QuickAccessItem>, modifier: Modifier = Modifier) {
    Column(modifier.padding(horizontal = 16.dp)) {
        Text(
            "Layanan Kami",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = item.action)
                        .padding(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = EWasteGreen,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(item.label, fontSize = 12.sp, textAlign = TextAlign.Center)
                }
            }
        }
    }
}


@Composable
fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
        modifier = modifier
    )
}

@Composable
fun NewsCard(newsItem: NewsItem, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = newsItem.imageUrl,
                    error = painterResource(id = R.drawable.ic_launcher_background) // Placeholder for error
                ),
                contentDescription = newsItem.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = newsItem.title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${newsItem.date} - ${newsItem.time}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun EWasteItemCard(eWaste: EWasteEntity, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = { /* TODO: Navigate to E-Waste detail screen */ }
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(
                    model = eWaste.imageUrl,
                    error = painterResource(id = R.drawable.ic_launcher_background), // Placeholder
                    placeholder = painterResource(id = R.drawable.ic_launcher_background) // Placeholder
                ),
                contentDescription = eWaste.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = eWaste.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Kategori: ${eWaste.category}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = eWaste.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Disposal: ${eWaste.disposalMethod}",
                    style = MaterialTheme.typography.bodySmall,
                    color = EWasteGreen,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}