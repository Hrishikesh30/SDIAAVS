package com.example.sdiaavs.ui.content

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sdiaavs.viewModel.AuthViewModel
import com.example.sdiaavs.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomePageContent(
    name: String?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = name?.let { "Welcome to Home Page! $it" } ?: "Loading...",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

    }
}
@Composable
fun HomePage(
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val uid = currentUser?.uid

    LaunchedEffect(uid) {
        if (uid != null && userViewModel.userData == null) {
            userViewModel.loadUserData(uid)
        }
    }

    val userData = userViewModel.userData

    HomePageContent(
        name = userData?.name,
    )
}
@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomePageContent(
        name = "test",
    )
}

