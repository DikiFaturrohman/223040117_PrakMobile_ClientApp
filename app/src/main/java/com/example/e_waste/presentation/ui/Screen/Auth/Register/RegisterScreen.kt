package com.example.e_waste.presentation.ui.Screen.Auth.Register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.e_waste.presentation.ui.theme.EWasteGreenBackground
import com.example.e_waste.presentation.ui.theme.EWasteGreenPrimary
import com.example.e_waste.presentation.ui.theme.EWasteGreyText
import com.example.e_waste.presentation.ui.viewmodels.AuthViewModel

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(authState.registrationSuccess) {
        if (authState.registrationSuccess) onRegisterSuccess()
    }

    LaunchedEffect(authState.error) {
        authState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    // Latar belakang utama halaman
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(EWasteGreenBackground)
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // Bagian Logo
                Icon(
                    imageVector = Icons.Default.Recycling,
                    contentDescription = "E-Waste Logo",
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
                Text(
                    "E-Waste",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Text(
                    "MANAGEMENT",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Card untuk Form
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        // Tab Masuk & Daftar
                        TabRow(
                            selectedTabIndex = 1, // Tab "Daftar" yang aktif
                            containerColor = Color.LightGray.copy(alpha = 0.2f),
                            contentColor = EWasteGreenPrimary,
                            indicator = {},
                            divider = {},
                            modifier = Modifier.clip(RoundedCornerShape(8.dp))
                        ) {
                            Tab(
                                selected = false,
                                onClick = onNavigateToLogin
                            ) {
                                Text(
                                    "Masuk",
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = EWasteGreyText
                                )
                            }
                            Tab(
                                selected = true,
                                onClick = { /* Biarkan kosong */ },
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (true) EWasteGreenPrimary else Color.Transparent)
                            ) {
                                Text(
                                    "Daftar",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = if (true) Color.White else EWasteGreyText
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Judul Form (sesuai kode asli)
                        Text("Buat Akun", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                        Text("Satu langkah mudah untuk memulai.", style = MaterialTheme.typography.bodyMedium, color = EWasteGreyText)

                        Spacer(modifier = Modifier.height(24.dp))

                        // Field Nama Lengkap
                        Text("Nama Lengkap :", style = MaterialTheme.typography.labelLarge)
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Nama Lengkap Anda") },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.LightGray,
                                focusedIndicatorColor = EWasteGreenPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Field Email
                        Text("Email :", style = MaterialTheme.typography.labelLarge)
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("email@anda.com") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.LightGray,
                                focusedIndicatorColor = EWasteGreenPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Field Nomor Telepon
                        Text("Nomor Telepon :", style = MaterialTheme.typography.labelLarge)
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("08123456789") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.LightGray,
                                focusedIndicatorColor = EWasteGreenPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Field Password
                        Text("Password :", style = MaterialTheme.typography.labelLarge)
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("••••••••") },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            singleLine = true,
                            trailingIcon = {
                                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                                val description = if (passwordVisible) "Sembunyikan" else "Tampilkan"
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(imageVector = image, description)
                                }
                            },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.LightGray,
                                focusedIndicatorColor = EWasteGreenPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Tombol Daftar
                        if (authState.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        } else {
                            Button(
                                onClick = {
                                    if (name.isNotBlank() && email.isNotBlank() && phone.isNotBlank() && password.isNotBlank()) {
                                        viewModel.register(name, email, phone, password)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = EWasteGreenPrimary)
                            ) {
                                Text("Daftar Akun", fontSize = 16.sp, color = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Link ke halaman Login
                Row(
                    modifier = Modifier.clickable { onNavigateToLogin() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Sudah punya akun? ", color = EWasteGreyText)
                    Text(
                        "Login",
                        fontWeight = FontWeight.Bold,
                        color = EWasteGreenPrimary
                    )
                }
            }
        }
    }
}