package com.example.e_waste.presentation.ui.Screen.Auth.Login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onForgotPassword: () -> Unit
) {
    // Assuming AuthViewModel has authState property
    var selectedTab by remember { mutableStateOf(0) } // 0 = Masuk, 1 = Daftar
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Custom colors matching the design
    val primaryGreen = Color(0xFF4ECDC4)
    val backgroundColor = Color(0xFF4ECDC4)
    val cardBackgroundColor = Color.White

    // Handle login/register actions
    fun handleLogin() {
        isLoading = true
        errorMessage = ""
        // Call your login logic here
        // viewModel.login(email, password)
        // For now, simulate success after delay
        isLoading = false
        onLoginSuccess()
    }

    fun handleRegister() {
        isLoading = true
        errorMessage = ""
        if (password != confirmPassword) {
            errorMessage = "Password tidak cocok"
            isLoading = false
            return
        }
        // Call your register logic here
        // viewModel.register(email, password)
        // For now, simulate success after delay
        isLoading = false
        onLoginSuccess()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top section with logo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo placeholder - replace with your actual logo
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                Color.White.copy(alpha = 0.2f),
                                RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "♻",
                            fontSize = 40.sp,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "E-Waste",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "MANAGEMENT",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        letterSpacing = 2.sp
                    )
                }
            }

            // Bottom section with form
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    // Tabs
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Masuk Tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedTab = 0 }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column {
                                Text(
                                    text = "Masuk",
                                    fontSize = 16.sp,
                                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == 0) Color.Black else Color.Gray
                                )
                                if (selectedTab == 0) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(2.dp)
                                            .background(primaryGreen)
                                    )
                                }
                            }
                        }

                        // Daftar Tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedTab = 1 }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column {
                                Text(
                                    text = "Daftar",
                                    fontSize = 16.sp,
                                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == 1) Color.Black else Color.Gray
                                )
                                if (selectedTab == 1) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(2.dp)
                                            .background(primaryGreen)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Welcome text
                    Text(
                        text = if (selectedTab == 0) "Halo!" else "Daftar Akun",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        text = if (selectedTab == 0)
                            "Sudah punya akun? Silakan login."
                        else
                            "Buat akun baru untuk memulai.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // Form fields
                    if (selectedTab == 0) {
                        // Login Form
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Alamat Email") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryGreen,
                                focusedLabelColor = primaryGreen
                            )
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Kata Sandi") },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                val image = if (passwordVisible)
                                    Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff

                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(imageVector = image, contentDescription = null)
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryGreen,
                                focusedLabelColor = primaryGreen
                            )
                        )

                        // Forgot Password
                        Text(
                            text = "Lupa Kata Sandi?",
                            color = primaryGreen,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .align(Alignment.End)
                                .clickable { onForgotPassword() }
                                .padding(bottom = 24.dp)
                        )

                        // Login Button
                        Button(
                            onClick = { handleLogin() },
                            enabled = email.isNotEmpty() && password.isNotEmpty() && !isLoading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryGreen),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White
                                )
                            } else {
                                Text(
                                    text = "Masuk",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Register link
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Belum punya akun? ",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Daftar",
                                color = primaryGreen,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.clickable { selectedTab = 1 }
                            )
                        }
                    } else {
                        // Register Form
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Alamat Email") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryGreen,
                                focusedLabelColor = primaryGreen
                            )
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Kata Sandi") },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                val image = if (passwordVisible)
                                    Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff

                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(imageVector = image, contentDescription = null)
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryGreen,
                                focusedLabelColor = primaryGreen
                            )
                        )

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Konfirmasi Kata Sandi") },
                            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                val image = if (confirmPasswordVisible)
                                    Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff

                                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                    Icon(imageVector = image, contentDescription = null)
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryGreen,
                                focusedLabelColor = primaryGreen
                            )
                        )

                        // Register Button
                        Button(
                            onClick = { handleRegister() },
                            enabled = email.isNotEmpty() && password.isNotEmpty() &&
                                    confirmPassword.isNotEmpty() && !isLoading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryGreen),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White
                                )
                            } else {
                                Text(
                                    text = "Daftar",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Login link
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Sudah punya akun? ",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Masuk",
                                color = primaryGreen,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.clickable { selectedTab = 0 }
                            )
                        }
                    }

                    // Error message
                    if (errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}