package com.example.cryptoapp.auth.useCase

import android.content.Intent
import com.example.cryptoapp.auth.service.AuthenticationService
import kotlinx.coroutines.flow.filterNotNull

class LoginWithGoogleAccountUseCase(private val service: AuthenticationService) {

    operator fun invoke(intent: Intent) = service.loginWithGoogleAccount(intent = intent).filterNotNull()
}