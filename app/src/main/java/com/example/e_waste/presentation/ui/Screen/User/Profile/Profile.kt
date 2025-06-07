package com.example.e_waste.presentation.ui.Screen.User.Profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.e_waste.R
import com.example.e_waste.presentation.ui.viewmodels.AuthViewModel
import com.example.e_waste.presentation.ui.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val profileState by profileViewModel.profileState.collectAsStateWithLifecycle()
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    val email by remember(profileState.user) { derivedStateOf { profileState.user?.email ?: "" } }

    var isEditing by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(authState.loggedInUserEmail, profileState.user) {
        val userEmail = authState.loggedInUserEmail
        if (userEmail != null && profileState.user == null) {
            profileViewModel.loadUserProfile(userEmail)
        }
    }

    LaunchedEffect(profileState.user) {
        profileState.user?.let {
            name = it.name
            phone = it.phone
        }
    }

    LaunchedEffect(profileState.error) {
        profileState.error?.let {
            snackbarHostState.showSnackbar(message = it)
            profileViewModel.clearError()
        }
    }

    LaunchedEffect(profileState.updateSuccess) {
        if (profileState.updateSuccess) {
            snackbarHostState.showSnackbar(message = "Profil berhasil diperbarui!")
            isEditing = false
            profileViewModel.resetUpdateStatus()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Profil Saya") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    if (isEditing) {
                        IconButton(onClick = {
                            profileState.user?.let {
                                name = it.name
                                phone = it.phone
                            }
                            isEditing = false
                        }) {
                            Icon(Icons.Default.Cancel, contentDescription = "Batal Edit")
                        }
                    } else {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Profil")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (profileState.isLoading && profileState.user == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (profileState.user != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground), // Ganti dengan avatar
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Profile Info Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ProfileTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = "Nama Lengkap",
                            icon = Icons.Default.Person,
                            enabled = isEditing
                        )
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        ProfileTextField(
                            value = email,
                            onValueChange = {},
                            label = "Email",
                            icon = Icons.Default.Email,
                            enabled = false // Email tidak bisa diubah
                        )
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        ProfileTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = "Nomor Telepon",
                            icon = Icons.Default.Phone,
                            enabled = isEditing,
                            keyboardType = KeyboardType.Phone
                        )
                    }
                }

                if (isEditing) {
                    Spacer(modifier = Modifier.height(24.dp))
                    if (profileState.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Button(
                            onClick = { profileViewModel.updateUserProfile(name, phone, email) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text("Simpan Perubahan")
                        }
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Gagal memuat profil.")
            }
        }
    }
}

@Composable
private fun ProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    enabled: Boolean,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null) },
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = if (enabled) MaterialTheme.colorScheme.primary else Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}