package com.example.e_waste.ui

// ForgotPasswordScreen.kt
@Composable
fun ForgotPasswordScreen(
    onResetEmailSent: (String) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val otpState by viewModel.otpState.collectAsState()
    var email by remember { mutableStateOf("") }

    LaunchedEffect(otpState) {
        if (otpState is AuthViewModel.OtpState.OtpSent) {
            onResetEmailSent(email)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Reset Password",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Enter your email address and we'll send you a code to reset your password",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = { viewModel.sendOtp(email) },
            enabled = email.isNotEmpty() && otpState !is AuthViewModel.OtpState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            if (otpState is AuthViewModel.OtpState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Send Reset Code")
            }
        }

        if (otpState is AuthViewModel.OtpState.Error) {
            Text(
                text = (otpState as AuthViewModel.OtpState.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}