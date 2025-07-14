package com.example.sdiaavs
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.sdiaavs.repository.AuthRepo
import com.example.sdiaavs.ui.content.HomePage
import com.example.sdiaavs.ui.auth.LoginPage
import com.example.sdiaavs.ui.content.MainScreen
import com.example.sdiaavs.viewModel.AuthViewModel
import com.example.sdiaavs.viewModel.AuthViewModelFactory
import com.example.sdiaavs.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(AuthRepo())  // Passing AuthRepo as dependency
    }
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Track whether the user is logged in or not
            var isLoggedIn by rememberSaveable { mutableStateOf(FirebaseAuth.getInstance().currentUser != null) }

            // Check if the user is logged in or not and show the appropriate page
            if (isLoggedIn) {
                // Show HomePage when logged in
                MainScreen(authViewModel = authViewModel,
                    userViewModel = userViewModel, onLogout = {
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
