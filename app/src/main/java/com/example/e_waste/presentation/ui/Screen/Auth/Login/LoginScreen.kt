package com.example.e_waste.presentation.ui.Screen.Auth.Login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
    var selectedTab by remember { mutableStateOf(0) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    fun handleLogin() {
        // ... (logika login)
        isLoading = true
        if (email.isNotEmpty() && password.isNotEmpty()) {
            onLoginSuccess()
        } else {
            errorMessage = "Email dan password tidak boleh kosong."
            isLoading = false
        }
    }

    fun handleRegister() {
        // ... (logika register)
        isLoading = true
        if (password != confirmPassword) {
            errorMessage = "Password tidak cocok"
            isLoading = false
            return
        }
        if (email.isNotEmpty() && password.isNotEmpty()) {
            onNavigateToRegister()
        } else {
            errorMessage = "Email dan password tidak boleh kosong."
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top section with logo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f), // PERUBAHAN: dari 0.4f menjadi 0.3f
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "E-Waste",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "MANAGEMENT",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onBackground,
                        letterSpacing = 2.sp
                    )
                }
            }

            // Bottom section with form
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f), // PERUBAHAN: dari 0.6f menjadi 0.7f
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Tabs
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TabItem(
                            text = "Masuk",
                            isSelected = selectedTab == 0,
                            onClick = { selectedTab = 0 }
                        )
                        TabItem(
                            text = "Daftar",
                            isSelected = selectedTab == 1,
                            onClick = { selectedTab = 1 }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = if (selectedTab == 0) "Halo!" else "Daftar Akun",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = if (selectedTab == 0) "Sudah punya akun? Silakan login." else "Buat akun baru untuk memulai.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // Form fields
                    if (selectedTab == 0) {
                        // Login Form
                        AuthTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Alamat Email",
                            keyboardType = KeyboardType.Email
                        )
                        PasswordTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = "Kata Sandi",
                            isVisible = passwordVisible,
                            onVisibilityChange = { passwordVisible = !passwordVisible }
                        )
                        Text(
                            text = "Lupa Kata Sandi?",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .align(Alignment.End)
                                .clickable { onForgotPassword() }
                                .padding(bottom = 24.dp)
                        )
                        AuthButton(
                            text = "Masuk",
                            isLoading = isLoading,
                            onClick = { handleLogin() }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        AuthSwitchLink(
                            promptText = "Belum punya akun? ",
                            linkText = "Daftar",
                            onClick = { selectedTab = 1 }
                        )
                    } else {
                        // Register Form
                        AuthTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Alamat Email",
                            keyboardType = KeyboardType.Email
                        )
                        PasswordTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = "Kata Sandi",
                            isVisible = passwordVisible,
                            onVisibilityChange = { passwordVisible = !passwordVisible }
                        )
                        PasswordTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = "Konfirmasi Kata Sandi",
                            isVisible = confirmPasswordVisible,
                            onVisibilityChange = { confirmPasswordVisible = !confirmPasswordVisible },
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                        AuthButton(
                            text = "Daftar",
                            isLoading = isLoading,
                            onClick = { handleRegister() }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        AuthSwitchLink(
                            promptText = "Sudah punya akun? ",
                            linkText = "Masuk",
                            onClick = { selectedTab = 0 }
                        )
                    }

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

@Composable
fun RowScope.TabItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(2.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
private fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isVisible: Boolean,
    onVisibilityChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (isVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            IconButton(onClick = onVisibilityChange) {
                Icon(imageVector = image, contentDescription = "Toggle password visibility")
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
private fun AuthButton(text: String, isLoading: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(8.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun AuthSwitchLink(promptText: String, linkText: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = promptText,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )
        Text(
            text = linkText,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable(onClick = onClick)
        )
    }
}