package com.example.cryptoapp.auth.domain.authentication

import com.example.cryptoapp.auth.data.AuthenticationRepository

class LoginWithEmailAndPasswordUseCase(private val repository: AuthenticationRepository) {

    operator fun invoke(email: String, password: String) = repository.loginWithEmailAndPassword(email = email, password = password)
}
