package com.example.sdiaavs.repository


import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepo( private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()) {
        suspend fun signIn(email: String, password: String): String? {
            return try {
                firebaseAuth.signInWithEmailAndPassword(email, password).await()
                firebaseAuth.uid
            } catch (e: Exception) {
                null
            }
        }

        suspend fun signUp(firstName: String, lastName: String, email: String, password: String): Boolean {
            return try {
                firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                true
            } catch (e: Exception) {
                false
            }
        }

        fun signOut() {
            firebaseAuth.signOut()
        }
    }
