package com.example.sdiaavs
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import com.example.sdiaavs.repository.AuthRepo
import com.example.sdiaavs.ui.auth.HomePage
import com.example.sdiaavs.ui.auth.LoginPage
import com.example.sdiaavs.viewModel.AuthViewModel
import com.example.sdiaavs.viewModel.AuthViewModelFactory

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(AuthRepo())  // Passing AuthRepo as dependency
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Track whether the user is logged in or not
            var isLoggedIn by remember { mutableStateOf(false) }

            // Check if the user is logged in or not and show the appropriate page
            if (isLoggedIn) {
                // Show HomePage when logged in
                HomePage(authViewModel = authViewModel, onLogout = {
                    isLoggedIn = false
                    // You can also handle navigation to the login screen here
                })
            } else {
                // Show LoginPage if not logged in
                LoginPage(
                    authViewModel = authViewModel,  // Pass the AuthViewModel to LoginPage
                    onLoginSuccess = {
                        isLoggedIn = true  // Set login state to true when login is successful
                    },
                    onSignupClick = {
                        // Navigate to Signup page if needed
                    }
                )
            }
        }
    }
}
