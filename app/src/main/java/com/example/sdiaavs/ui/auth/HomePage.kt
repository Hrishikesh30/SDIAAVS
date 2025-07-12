package com.example.sdiaavs.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sdiaavs.viewModel.AuthViewModel
import com.example.sdiaavs.viewModel.UserViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun HomePage(authViewModel: AuthViewModel,userViewModel: UserViewModel, onLogout: () -> Unit) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val uid = currentUser?.uid
    println("*************$uid")
    // Load user data once when UID is available
    LaunchedEffect(uid) {
        if (uid != null && userViewModel.userData == null) {
            userViewModel.loadUserData(uid)
        }
    }

    val userData = userViewModel.userData
    println("*************$userData")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (userData != null)
                "Welcome to Home Page! ${userData.name}"
            else
                "Loading...",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                authViewModel.signOut()  // Call signOut from the ViewModel
                onLogout()  // Notify the parent composable (e.g., navigate to login screen)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Logout")
        }
    }
}
