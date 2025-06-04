package com.example.e_waste.presentation.ui.Screen.Auth.Otp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.e_waste.presentation.ui.viewmodels.AuthViewModel

@Composable
fun OtpVerificationScreen(
    email: String,
    isForRegistration: Boolean,
    onVerificationSuccessForRegistration: () -> Unit,
    onVerificationSuccessForPasswordReset: (String) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var otp by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Reset state when screen is disposed or left
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP ) {
                if (!authState.otpVerified) { // Don't reset if navigating away due to success
                    viewModel.resetAuthState()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(authState.otpVerified) {
        if (authState.otpVerified) {
            if (isForRegistration) {
                onVerificationSuccessForRegistration()
            } else {
                onVerificationSuccessForPasswordReset(email) // Pass email to reset password screen
            }
            viewModel.resetAuthState() // Reset state after navigation
        }
    }

    LaunchedEffect(authState.error) {
        authState.error?.let {
            snackbarHostState.showSnackbar(message = it)
            viewModel.clearError()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Verify OTP",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "An OTP has been sent to $email. Please enter it below.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = otp,
                onValueChange = { if (it.length <= 6) otp = it }, // Assuming OTP is 6 digits
                label = { Text("OTP Code") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (authState.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        if (otp.isNotBlank() && email.isNotBlank()) {
                            viewModel.verifyOtp(email, otp)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = otp.length == 6 // Example: enable only if OTP has 6 digits
                ) {
                    Text("Verify OTP")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = {
                // Resend OTP logic
                viewModel.sendOtp(email) // Re-use sendOtp
            }) {
                Text("Resend OTP")
            }
        }
    }
}