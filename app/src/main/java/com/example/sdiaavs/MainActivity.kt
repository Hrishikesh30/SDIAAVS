package com.example.sdiaavs
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sdiaavs.repository.AuthRepo
import com.example.sdiaavs.ui.content.HomePage
import com.example.sdiaavs.ui.auth.LoginPage
import com.example.sdiaavs.ui.content.MainScreen
import com.example.sdiaavs.ui.content.admin.AdminLoginPage
import com.example.sdiaavs.viewModel.AuthViewModel
import com.example.sdiaavs.viewModel.AuthViewModelFactory
import com.example.sdiaavs.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.sdiaavs.ui.content.ChangePasswordPage

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(AuthRepo())  // Passing AuthRepo as dependency
    }
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val startDestination = if (FirebaseAuth.getInstance().currentUser != null) {
                "userHome"
            } else {
                "userLogin"
            }
            androidx.navigation.compose.NavHost(
                navController = navController,
                startDestination = startDestination
            ) {
                composable("userLogin") {
                    LoginPage(
                        authViewModel = authViewModel,
                        onLoginSuccess = {
                            navController.navigate("userHome") {
                                popUpTo("userLogin") { inclusive = true }
                            }
                        },
                        onSignupClick = {
                            navController.navigate("signup")
                        },
                        navController = navController // Pass controller for "Admin? Login here"
                    )
                }

                composable("adminLogin") {
                    AdminLoginPage(
                        navController = navController
                    )
                }

                composable("userHome") {
                    MainScreen(
                        authViewModel = authViewModel,
                        userViewModel = userViewModel,
                        onLogout = {
                            FirebaseAuth.getInstance().signOut()
                            navController.navigate("userLogin") {
                                popUpTo("userHome") { inclusive = true }
                            }
                        },
                        navController = navController // Pass navController
                    )
                }

                composable("changePassword") {
                    ChangePasswordPage(
                        authViewModel = authViewModel,
                        onPasswordChanged = {
                            navController.popBackStack() // Go back to profile after change
                        }
                    )
                }

//                composable("adminHome") {
//                    AdminDashboardScreen(
//                        onLogout = {
//                            FirebaseAuth.getInstance().signOut()
//                            navController.navigate("adminLogin") {
//                                popUpTo("adminHome") { inclusive = true }
//                            }
//                        }
//                    )
//                }
//
//                composable("signup") {
//                    SignupScreen(
//                        onSignupSuccess = {
//                            navController.navigate("userHome") {
//                                popUpTo("signup") { inclusive = true }
//                            }
//                        }
//                    )
//                }
            }
        }
    }
}
