package com.example.sdiaavs.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sdiaavs.dataModel.UserData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class UserViewModel : ViewModel() {

    private var _userData = mutableStateOf<UserData?>(null)
    val userData: UserData? get() = _userData.value

    fun loadUserData(uid: String) {
        if (_userData.value != null) return  // Already fetched
        Firebase.firestore.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(UserData::class.java)
                    println("✅ User fetched: $user")
                    _userData.value = user
                } else {
                    println("Document with UID $uid does NOT exist.")
                }
            }
            .addOnFailureListener { exception ->
                println(" Error fetching user: ${exception.message}")
            }
    }

    fun updateUserData(uid: String, updatedData: UserData, onComplete: () -> Unit) {
        Firebase.firestore.collection("users").document(uid).set(updatedData)
            .addOnSuccessListener {
                println("✅ User data updated successfully.")
                onComplete()
            }
            .addOnFailureListener { exception ->
                println("Error updating user data: ${exception.message}")
            }
    }


    fun clearUserData() {
        _userData.value = null
    }
}