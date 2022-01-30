package com.example.cryptoapp.domain.authentication

import com.example.cryptoapp.data.repository.AuthenticationRepository

class LoginWithEmailAndPasswordUseCase(private val repository: AuthenticationRepository) {

    operator fun invoke(email: String, password: String) = repository.loginWithEmailAndPassword(email = email, password = password)
}
