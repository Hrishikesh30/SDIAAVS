package com.example.sdiaavs.ui.content

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sdiaavs.viewModel.AuthViewModel
import com.example.sdiaavs.viewModel.UserViewModel


@Composable
fun ProfilePage(
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Profile Page", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                authViewModel.signOut()
                onLogoutClick()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Logout")
        }
    }
}
