package com.example.cryptoapp.data.service

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface Authentication {

    fun registerWithEmailAndPassWord(email: String, password: String): Task<AuthResult>

    fun loginWithEmailAndPassword(email: String, password: String): Task<AuthResult>

    fun logOut()

    fun resetPassword(email: String): Task<Void>

    fun getCurrentUser(): FirebaseUser?
}
