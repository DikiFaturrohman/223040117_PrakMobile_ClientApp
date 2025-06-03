package com.example.e_waste.presentation.ui.Screen.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.e_waste.presentation.ui.theme.EWasteGreen
import com.example.e_waste.presentation.ui.theme.EWasteWhite

// Data class untuk item Berita (dummy)
data class NewsItem(
    val title: String,
    val date: String,
    val time: String,
    val imageUrl: String
)

// MainScreen Composable yang telah diperbarui
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToProfile: () -> Unit,
    onLogout: () -> Unit,
) {
    // Dummy data untuk berita
    val newsItems = listOf(
        NewsItem(
            "Recycle Lebih Mudah: Fitur Baru Penjadwalan...",
            "16th May",
            "09:32 pm",
            "https://d1vbn70lmn1nqe.cloudfront.net/prod/wp-content/uploads/2021/10/27040338/Ini-Pentingnya-Mendaur-Ulang-Sampah-Elektronik.jpg"
        ),
        NewsItem(
            "Anak Muda Peduli E-Waste: 5.000+ Pengguna Aktif ...",
            "11th May",
            "10:15 am",
            "https://dislh.surabaya.go.id/source/post/large/IMG-20220224-WA0009.jpg"
        )
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar()
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            item {
                MainHeader()
            }
            item {
                OurServices(modifier = Modifier.padding(horizontal = 16.dp))
            }
            item {
                NewsSectionHeader(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            }
            items(newsItems) { news ->
                NewsCard(news = news, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            }
        }
    }
}

@Composable
fun MainHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = EWasteGreen,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo
                Text(
                    text = "E-Waste\nMANAGEMENT",
                    color = EWasteWhite,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Start,
                    lineHeight = 14.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifikasi", tint = EWasteWhite)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Greeting
            Text("Halo,", color = EWasteWhite, fontSize = 24.sp)
            Text("Nama!", color = EWasteWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            // Electronic Point Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AccountCircle, contentDescription = "Electronic Point", tint = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Electronic Point:", fontSize = 14.sp, color = Color.Gray)
                        Text("10.000", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = { /*TODO*/ },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EWasteGreen)
                    ) {
                        Text("Withdraw", color = EWasteWhite)
                    }
                }
            }
        }
    }
}

@Composable
fun OurServices(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(top = 24.dp)) {
        Text("Our Services", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ServiceItem(icon = Icons.Default.LocalShipping, text = "Waste & get Point")
            ServiceItem(icon = Icons.Default.Lightbulb, text = "Tips")
            ServiceItem(icon = Icons.Default.HeadsetMic, text = "Customer Service")
        }
    }
}

@Composable
fun ServiceItem(icon: ImageVector, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.White)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = text, tint = EWasteGreen)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text, fontSize = 12.sp, textAlign = TextAlign.Center)
    }
}

@Composable
fun NewsSectionHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("News", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        TextButton(onClick = { /*TODO*/ }) {
            Text("View All", color = EWasteGreen)
        }
    }
}

@Composable
fun NewsCard(news: NewsItem, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(model = news.imageUrl),
                contentDescription = news.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(news.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(news.date, fontSize = 12.sp, color = Color.Gray)
                    Text(news.time, fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    var selectedItem by remember { mutableStateOf(0) }
    // --- PERUBAHAN DI SINI ---
    val items = listOf(
        "Home" to Icons.Default.Home,
        "Kategori" to Icons.Default.Category, // Mengganti "Pesan"
        "Profil" to Icons.Default.AccountCircle    // Mengganti "My History"
    )

    NavigationBar(
        containerColor = Color.White
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.second, contentDescription = item.first) },
                label = { Text(item.first) },
                selected = selectedItem == index,
                onClick = { selectedItem = index },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = EWasteGreen,
                    selectedTextColor = EWasteGreen,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = EWasteGreen.copy(alpha = 0.1f)
                )
            )
        }
    }
}