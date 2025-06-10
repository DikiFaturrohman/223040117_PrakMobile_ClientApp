package com.example.e_waste.presentation.ui.Screen.Auth.Login

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
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(authState.loginSuccess) {
        if (authState.loginSuccess) onLoginSuccess()
    }

    LaunchedEffect(authState.error) {
        authState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

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

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        TabRow(
                            selectedTabIndex = 0, // Tab "Masuk" yang aktif
                            containerColor = Color.LightGray.copy(alpha = 0.2f),
                            contentColor = EWasteGreenPrimary,
                            indicator = {},
                            divider = {},
                            modifier = Modifier.clip(RoundedCornerShape(8.dp))
                        ) {
                            Tab(
                                selected = true,
                                onClick = { /* Biarkan kosong */ },
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (true) EWasteGreenPrimary else Color.Transparent)
                            ) {
                                Text(
                                    "Masuk",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = if (true) Color.White else EWasteGreyText
                                )
                            }
                            Tab(
                                selected = false,
                                onClick = onNavigateToRegister
                            ) {
                                Text(
                                    "Daftar",
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = EWasteGreyText
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text("Halo!", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                        Text("Sudah punya akun? Silakan login.", style = MaterialTheme.typography.bodyMedium, color = EWasteGreyText)

                        Spacer(modifier = Modifier.height(24.dp))

                        val textFieldColors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EWasteGreenPrimary,
                            unfocusedBorderColor = Color.LightGray,
                            cursorColor = EWasteGreenPrimary,
                            focusedLabelColor = EWasteGreenPrimary,
                            unfocusedLabelColor = EWasteGreyText,
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Alamat Email") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            singleLine = true,
                            colors = textFieldColors
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Kata Sandi") },
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
                            colors = textFieldColors
                        )

                        TextButton(
                            onClick = { /* TODO: Lupa Password */ },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Lupa Kata Sandi?", color = EWasteGreenPrimary, style = MaterialTheme.typography.bodySmall)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (authState.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        } else {
                            Button(
                                onClick = {
                                    if (email.isNotBlank() && password.isNotBlank()) {
                                        viewModel.login(email, password)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = EWasteGreenPrimary)
                            ) {
                                Text("Masuk", fontSize = 16.sp, color = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.clickable { onNavigateToRegister() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Belum punya akun? ", color = EWasteGreyText)
                    Text(
                        "Daftar",
                        fontWeight = FontWeight.Bold,
                        color = EWasteGreenPrimary
                    )
                }
            }
        }
    }
}