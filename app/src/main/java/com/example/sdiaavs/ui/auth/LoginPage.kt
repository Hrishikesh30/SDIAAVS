package com.example.sdiaavs.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sdiaavs.viewModel.AuthViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sdiaavs.R

@Composable
fun LoginContent(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit,
    errorMessage: String,
    navController: NavController
) {    Box(modifier = Modifier
    .fillMaxSize()
    .padding(24.dp)) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.Center),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.sdiaavs4),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(300.dp)
                .clip(CircleShape)
        )

        Image(
            painter = painterResource(id = R.drawable.sdiaavs1),
            contentDescription = "Banner",
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp) // avoid pushing layout
        )

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        // Login Button
        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        // Signup Link
        TextButton(onClick = onSignupClick) {
            Text("Don't have an account? Sign up")
        }
        TextButton(
            onClick = { navController.navigate("adminLogin") },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Admin? Login here", style = TextStyle(color = Color.Blue))
        }
        // Error message
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

}
@Composable
fun LoginPage(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onSignupClick: () -> Unit,
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }


    LoginContent(
        email = email,
        password = password,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onLoginClick = {
            authViewModel.login(email, password) { success, _ ->
                if (success) onLoginSuccess() else error = "Login failed!"
            }
        },

        onSignupClick = onSignupClick,
        errorMessage = error,
        navController = navController
    )
}
@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginContent(
        email = "test@example.com",
        password = "password123",
        onEmailChange = {},
        onPasswordChange = {},
        onLoginClick = {},
        onSignupClick = {},
        errorMessage = "",
        navController = rememberNavController()
    )
}
