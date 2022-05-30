package com.example.cryptoapp.auth.service

import com.example.cryptoapp.auth.AuthResult
import com.example.cryptoapp.auth.R
import com.example.cryptoapp.auth.consumeTask
import com.example.cryptoapp.auth.service.model.User
import com.example.cryptoapp.auth.service.model.UserAvatarType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

class AuthenticationServiceImpl : AuthenticationService {

    private val firebaseAuth = FirebaseAuth.getInstance()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun loginWithEmailAndPassword(email: String, password: String): Flow<AuthResult> = consumeTask(
        task = firebaseAuth.signInWithEmailAndPassword(email, password)
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun registerWithEmailAndPassWord(email: String, password: String): Flow<AuthResult> = consumeTask(
        task = firebaseAuth.createUserWithEmailAndPassword(email, password)
    )

    override fun logOut() = firebaseAuth.signOut()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun resetPassword(email: String): Flow<AuthResult> = consumeTask(
        task = firebaseAuth.sendPasswordResetEmail(email)
    )

    override fun getCurrentUser(): User? = firebaseAuth.currentUser?.toModel()

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
