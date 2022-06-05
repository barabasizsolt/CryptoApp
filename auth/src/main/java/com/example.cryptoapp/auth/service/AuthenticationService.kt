package com.example.cryptoapp.auth.service

import com.example.cryptoapp.auth.AuthResult
import com.example.cryptoapp.auth.service.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow

interface AuthenticationService {

    val firebaseAuth: FirebaseAuth

    fun loginWithEmailAndPassword(email: String, password: String): Flow<AuthResult>

    fun registerWithEmailAndPassWord(email: String, password: String): Flow<AuthResult>

    fun logOut()

    fun resetPassword(email: String): Flow<AuthResult>

    fun getCurrentUser(): User?
}
