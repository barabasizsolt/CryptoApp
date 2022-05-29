package com.example.cryptoapp.firebase.domain.auth

import com.example.cryptoapp.firebase.data.auth.AuthenticationRepository

class RegisterWithEmailAndPasswordUseCase(private val repository: AuthenticationRepository) {

    operator fun invoke(email: String, password: String) = repository.registerWithEmailAndPassWord(email = email, password = password)
}
