package com.example.cryptoapp.data.repository

import com.example.cryptoapp.R
import com.example.cryptoapp.data.manager.AuthenticationManager
import com.example.cryptoapp.data.model.auth.User
import com.example.cryptoapp.data.model.auth.UserAvatarType
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

class AuthenticationRepository(private val manager: AuthenticationManager) {

    fun loginWithEmailAndPassword(email: String, password: String): Task<AuthResult> =
        manager.loginWithEmailAndPassword(email = email, password = password)

    fun registerWithEmailAndPassWord(email: String, password: String): Task<AuthResult> =
        manager.registerWithEmailAndPassWord(email = email, password = password)

    fun logOut() = manager.logOut()

    fun resetPassword(email: String): Task<Void> = manager.resetPassword(email = email)

    fun getCurrentUser(): User = manager.getCurrentUser()?.toModel() ?: throw IllegalStateException("No user found")

    private fun FirebaseUser.toModel() = when {
        email == null || metadata?.creationTimestamp == null -> null
        else -> User(
            userId = uid,
            avatarType = if (photoUrl == null) UserAvatarType.IntType(id = R.drawable.ic_avatar) else UserAvatarType.UriType(uri = photoUrl!!),
            email = email!!,
            registrationTimeStamp = metadata!!.creationTimestamp
        )
    }
}
