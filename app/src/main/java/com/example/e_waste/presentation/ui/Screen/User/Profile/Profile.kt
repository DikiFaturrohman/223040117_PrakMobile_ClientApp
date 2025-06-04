package com.example.e_waste.presentation.ui.Screen.User.Profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource // For placeholder
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.e_waste.R // Ensure you have a placeholder avatar
import com.example.e_waste.data.entity.UserEntity
import com.example.e_waste.presentation.ui.theme.EWasteGreen
import com.example.e_waste.presentation.ui.theme.EWasteWhite
import com.example.e_waste.presentation.ui.viewmodels.ProfileViewModel
import com.example.e_waste.presentation.ui.viewmodels.AuthViewModel // Assuming AuthViewModel holds current user email


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToChangePassword: () -> Unit, // Callback baru untuk navigasi ganti password
    // We need to know the current user's email. This could be passed as an argument
    // or retrieved from AuthViewModel if it stores the logged-in user's email.
    authViewModel: AuthViewModel = hiltViewModel(), // To get current user's email
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val profileState by profileViewModel.profileState.collectAsStateWithLifecycle()
    val authState by authViewModel.authState.collectAsStateWithLifecycle() // To get user's email

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    val email by remember(profileState.user) { derivedStateOf { profileState.user?.email ?: "" } }

    var isEditing by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Load profile when email is available and user is not loaded
    LaunchedEffect(authState.loggedInUserEmail, profileState.user) {
        val userEmail = authState.loggedInUserEmail
        if (userEmail != null && profileState.user == null) {
            profileViewModel.loadUserProfile(userEmail)
        }
    }

    // Update local states when user data is loaded from ViewModel
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
            snackbarHostState.showSnackbar(
                message = "Profile updated successfully!",
                duration = SnackbarDuration.Short
            )
            isEditing = false // Exit editing mode on successful update
            profileViewModel.resetUpdateStatus()
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                // Reset non-persistent states if needed, e.g., editing mode
                // isEditing = false // Or based on your app's logic
                profileViewModel.resetUpdateStatus()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("My Profile", color = EWasteWhite) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = EWasteWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = EWasteGreen),
                actions = {
                    if (!isEditing) {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit Profile", tint = EWasteWhite)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (profileState.isLoading && profileState.user == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (profileState.user == null && authState.loggedInUserEmail == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Could not load user profile. Please log in again.")
            }
        }
        else if (profileState.user != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Picture Placeholder
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground), // Replace with actual image logic
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isEditing,
                    colors = if (!isEditing) TextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledIndicatorColor = MaterialTheme.colorScheme.outline,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ) else TextFieldDefaults.colors()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { /* Email is not editable */ },
                    label = { Text("Email Address") },
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false, // Email typically not editable from profile
                    colors = TextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledIndicatorColor = MaterialTheme.colorScheme.outline,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    enabled = isEditing,
                    colors = if (!isEditing) TextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledIndicatorColor = MaterialTheme.colorScheme.outline,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ) else TextFieldDefaults.colors()
                )
                Spacer(modifier = Modifier.height(32.dp))

                if (isEditing) {
                    if (profileState.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Button(
                            onClick = {
                                profileViewModel.updateUserProfile(name, phone, email)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Save Changes")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = {
                            isEditing = false
                            // Reset fields to original values from ViewModel state if changes are cancelled
                            profileState.user?.let {
                                name = it.name
                                phone = it.phone
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancel")
                    }
                } else {
                    OutlinedButton(
                        onClick = onNavigateToChangePassword,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Change Password")
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Loading profile...") // Or a more specific message if email is missing
            }
        }
    }
}