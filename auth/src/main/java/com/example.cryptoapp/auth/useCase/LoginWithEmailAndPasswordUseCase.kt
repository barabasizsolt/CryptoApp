package com.example.cryptoapp.auth.useCase

import com.example.cryptoapp.auth.AuthResult
import com.example.cryptoapp.auth.service.AuthenticationService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

class LoginWithEmailAndPasswordUseCase(private val service: AuthenticationService) {

    operator fun invoke(email: String, password: String): Flow<AuthResult> =
        service.loginWithEmailAndPassword(email = email, password = password).filterNotNull()
}
