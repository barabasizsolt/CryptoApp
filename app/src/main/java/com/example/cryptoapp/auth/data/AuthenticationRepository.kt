package com.example.cryptoapp.auth.data

import com.example.cryptoapp.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

class AuthenticationRepository(private val source: AuthenticationSource) {

    fun loginWithEmailAndPassword(email: String, password: String): Task<AuthResult> =
        source.loginWithEmailAndPassword(email = email, password = password)

    fun registerWithEmailAndPassWord(email: String, password: String): Task<AuthResult> =
        source.registerWithEmailAndPassWord(email = email, password = password)

    fun logOut() = source.logOut()

    fun resetPassword(email: String): Task<Void> = source.resetPassword(email = email)

    fun getCurrentUser(): User = source.getCurrentUser()?.toModel() ?: throw IllegalStateException("No user found")

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
