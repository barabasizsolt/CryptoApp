package com.example.cryptoapp.auth.service

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.cryptoapp.auth.AuthResult
import com.example.cryptoapp.auth.AuthWithResult
import com.example.cryptoapp.auth.R
import com.example.cryptoapp.auth.consumeTask
import com.example.cryptoapp.auth.consumeTaskWithResult
import com.example.cryptoapp.auth.service.model.User
import com.example.cryptoapp.auth.service.model.UserAvatarType
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.transform

class AuthenticationServiceImpl : AuthenticationService {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleAuth: GoogleSignInClient

    override fun initialize(context: Context) {

        val request = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(WEB_CLIENT_ID)
            .requestEmail()
            .build()

        googleAuth = GoogleSignIn.getClient(context, request)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun loginWithEmailAndPassword(email: String, password: String): Flow<AuthResult> = consumeTask(
        task = firebaseAuth.signInWithEmailAndPassword(email, password)
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun registerWithEmailAndPassWord(email: String, password: String): Flow<AuthResult> = consumeTask(
        task = firebaseAuth.createUserWithEmailAndPassword(email, password)
    )

    override fun getIntentForGoogleAccountLogin(): Intent {
        return googleAuth.signInIntent
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun loginWithGoogleAccount(intent: Intent): Flow<AuthResult> = consumeTaskWithResult(
        task = GoogleSignIn.getSignedInAccountFromIntent(intent),
        taskConverter = { account -> GoogleAuthProvider.getCredential(account.idToken, null) }
    ).transform { result ->
        when (result) {
            is AuthWithResult.Success ->
                consumeTask(task = firebaseAuth.signInWithCredential(result.data)).collect { res -> emit(res) }
            is AuthWithResult.Failure ->
                emit(value = AuthResult.Failure(error = result.error))
        }
    }

    override fun logOut(): Flow<AuthResult> = consumeTask(
        task = googleAuth.signOut()
    ).also { firebaseAuth.signOut() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun resetPassword(email: String): Flow<AuthResult> = consumeTask(
        task = firebaseAuth.sendPasswordResetEmail(email)
    )

    override fun getCurrentUser(): User? = firebaseAuth.currentUser?.toModel()

    private fun FirebaseUser?.toModel() = when {
        this == null || email == null || metadata?.creationTimestamp == null -> null
        else -> User(
            userId = uid,
            avatarType = if (photoUrl == null) UserAvatarType.IntType(id = R.drawable.ic_avatar) else UserAvatarType.UriType(uri = photoUrl!!),
            email = email!!,
            registrationTimeStamp = metadata!!.creationTimestamp
        )
    }

    companion object {
        private const val WEB_CLIENT_ID: String = "147156823490-9670iuhk2r64dnoh9nn52o7jf2ifeakf.apps.googleusercontent.com"
    }
}
