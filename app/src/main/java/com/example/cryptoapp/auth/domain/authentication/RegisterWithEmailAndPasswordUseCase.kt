package com.example.cryptoapp.auth.domain.authentication

import com.example.cryptoapp.auth.data.AuthenticationRepository

class RegisterWithEmailAndPasswordUseCase(private val repository: AuthenticationRepository) {

    operator fun invoke(email: String, password: String) = repository.registerWithEmailAndPassWord(email = email, password = password)
}
