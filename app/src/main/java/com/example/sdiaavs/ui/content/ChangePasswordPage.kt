package com.example.sdiaavs.ui.content

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.sdiaavs.viewModel.AuthViewModel

@Composable
fun ChangePasswordPage(
    authViewModel: AuthViewModel,
    onPasswordChanged: () -> Unit
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Change Password", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = oldPassword,
            onValueChange = { oldPassword = it },
            label = { Text("Old Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm New Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        if (errorMessage != null) {
            Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (successMessage != null) {
            Text(successMessage!!, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
        }
        Button(
            onClick = {
                errorMessage = null
                successMessage = null
                if (newPassword != confirmPassword) {
                    errorMessage = "Passwords do not match."
                    return@Button
                }
                if (newPassword.length < 6) {
                    errorMessage = "Password must be at least 6 characters."
                    return@Button
                }
                isLoading = true
                // Firebase requires re-authentication for sensitive actions
                val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
                val email = user?.email
                if (user != null && email != null) {
                    val credential = com.google.firebase.auth.EmailAuthProvider.getCredential(email, oldPassword)
                    user.reauthenticate(credential)
                        .addOnSuccessListener {
                            authViewModel.updatePassword(user.uid, newPassword) { success ->
                                isLoading = false
                                if (success) {
                                    successMessage = "Password changed successfully."
                                    onPasswordChanged()
                                } else {
                                    errorMessage = "Failed to change password."
                                }
                            }
                        }
                        .addOnFailureListener {
                            isLoading = false
                            errorMessage = "Old password is incorrect."
                        }
                } else {
                    isLoading = false
                    errorMessage = "User not found."
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                Text("Change Password")
            }
        }
    }
} 