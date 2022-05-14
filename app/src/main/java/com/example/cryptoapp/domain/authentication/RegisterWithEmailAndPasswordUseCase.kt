package com.example.cryptoapp.domain.authentication

import com.example.cryptoapp.auth.AuthenticationRepository

class RegisterWithEmailAndPasswordUseCase(private val repository: AuthenticationRepository) {

    operator fun invoke(email: String, password: String) = repository.registerWithEmailAndPassWord(email = email, password = password)
}
