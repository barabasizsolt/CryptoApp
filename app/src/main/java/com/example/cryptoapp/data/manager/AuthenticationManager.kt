package com.example.cryptoapp.data.manager

import com.example.cryptoapp.data.service.Authentication
import com.google.firebase.auth.FirebaseAuth

class AuthenticationManager : Authentication {

    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun registerWithEmailAndPassWord(email: String, password: String) =
        firebaseAuth.createUserWithEmailAndPassword(email, password)

    override fun loginWithEmailAndPassword(email: String, password: String) =
        firebaseAuth.signInWithEmailAndPassword(email, password)

    override fun logOut() = firebaseAuth.signOut()

    override fun resetPassword(email: String) = firebaseAuth.sendPasswordResetEmail(email)

    override fun getCurrentUser() = firebaseAuth.currentUser
}
