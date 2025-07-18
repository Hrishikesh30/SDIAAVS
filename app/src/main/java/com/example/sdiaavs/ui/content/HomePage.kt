package com.example.sdiaavs.ui.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sdiaavs.viewModel.AuthViewModel
import com.example.sdiaavs.viewModel.UserViewModel

@Composable
fun HomePageContent(
    name: String?,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = name?.let { "आपका स्वागत है ! $it" } ?: "Loading...",
            style = MaterialTheme.typography.headlineMedium
        )
        Button(onClick = onLogoutClick) {
            Text("Logout")
        }
        Spacer(modifier = Modifier.height(16.dp))

    }
}
@Composable
fun HomePage(
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    onLogoutClick: () -> Unit
) {
    val userData by userViewModel.userData

    HomePageContent(
        name = userData?.name,
        onLogoutClick = {
            authViewModel.signOut()
            onLogoutClick()
        },
    )
}
@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomePageContent(
        name = "test",
        onLogoutClick = {},
    )
}

