package com.example.cryptoapp.firebase.domain

import com.example.cryptoapp.firebase.data.AuthenticationRepository

class RegisterWithEmailAndPasswordUseCase(private val repository: AuthenticationRepository) {

    operator fun invoke(email: String, password: String) = repository.registerWithEmailAndPassWord(email = email, password = password)
}
