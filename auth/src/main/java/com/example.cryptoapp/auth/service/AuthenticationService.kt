package com.example.cryptoapp.auth.service

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.cryptoapp.auth.AuthResult
import com.example.cryptoapp.auth.service.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow

interface AuthenticationService {

    fun initialize(context: Context)

    fun loginWithEmailAndPassword(email: String, password: String): Flow<AuthResult>

    fun registerWithEmailAndPassWord(email: String, password: String): Flow<AuthResult>

    fun loginWithGoogleAccount(intent: Intent): Flow<AuthResult>

    fun getIntentForGoogleAccountLogin(): Intent

    fun logOut(): Flow<AuthResult>

    fun resetPassword(email: String): Flow<AuthResult>

    fun getCurrentUser(): User?
}
