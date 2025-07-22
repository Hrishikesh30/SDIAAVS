package com.example.sdiaavs.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sdiaavs.repository.AuthRepo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepo: AuthRepo = AuthRepo()) : ViewModel() {
    val uid: String? get() = FirebaseAuth.getInstance().currentUser?.uid

    // Function for login
    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            // Call the signIn method from AuthRepo
            val uid = authRepo.signIn(email, password)
            onResult(uid != null, uid)
        }
    }

    // Function for signup
    fun signUp(firstName: String, lastName: String, email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            // Call the signUp method from AuthRepo
            val success = authRepo.signUp(firstName, lastName, email, password)
            onResult(success)
        }
    }
    fun updatePassword(uid: String, newPassword: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = authRepo.updatePassword(newPassword)
            onComplete(result)
        }
    }

    // Function for sign out
    fun signOut() {
        authRepo.signOut()
    }
}


