package com.example.cryptoapp.data.repository

import com.example.cryptoapp.data.manager.AuthenticationManager
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

class AuthenticationRepository(private val manager: AuthenticationManager) {

    fun loginWithEmailAndPassword(email: String, password: String) : Task<AuthResult> =
        manager.loginWithEmailAndPassword(email = email, password = password)

    fun registerWithEmailAndPassWord(email: String, password: String) : Task<AuthResult> =
        manager.registerWithEmailAndPassWord(email = email, password = password)

    fun logOut() = manager.logOut()

    fun resetPassword(email: String): Task<Void> = manager.resetPassword(email = email)

    fun getCurrentUser() : FirebaseUser = manager.getCurrentUser() ?: throw IllegalStateException("No user found")
}
