package com.example.e_waste.presentation.ui.Screen.Auth.ResetPassword

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.e_waste.presentation.ui.viewmodels.AuthViewModel

@Composable
fun ResetPasswordScreen(
    email: String, // Email that has been OTP verified
    onPasswordResetSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var localError by remember { mutableStateOf<String?>(null) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                if (!authState.passwordResetSuccess) {
                    viewModel.resetAuthState()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(authState.passwordResetSuccess) {
        if (authState.passwordResetSuccess) {
            onPasswordResetSuccess()
            viewModel.resetAuthState() // Reset after navigation
        }
    }

    LaunchedEffect(authState.error) {
        authState.error?.let {
            snackbarHostState.showSnackbar(message = it)
            viewModel.clearError()
        }
    }
    LaunchedEffect(localError) {
        localError?.let {
            snackbarHostState.showSnackbar(message = it)
            localError = null // Clear error after showing
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Reset Password",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Enter your new password for $email.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New Password") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmNewPassword,
                onValueChange = { confirmNewPassword = it },
                label = { Text("Confirm New Password") },
                singleLine = true,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = image, contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (authState.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        localError = null // Clear previous local errors
                        if (newPassword.isBlank() || confirmNewPassword.isBlank()) {
                            localError = "Password fields cannot be empty."
                            return@Button
                        }
                        if (newPassword.length < 6) { // Example validation
                            localError = "Password must be at least 6 characters."
                            return@Button
                        }
                        if (newPassword != confirmNewPassword) {
                            localError = "Passwords do not match."
                            return@Button
                        }
                        viewModel.resetPassword(email, newPassword)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reset Password")
                }
            }
        }
    }
}