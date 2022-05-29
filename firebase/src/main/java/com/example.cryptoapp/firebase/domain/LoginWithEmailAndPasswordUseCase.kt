package com.example.cryptoapp.firebase.domain

import com.example.cryptoapp.firebase.data.AuthenticationRepository

class LoginWithEmailAndPasswordUseCase(private val repository: AuthenticationRepository) {

    operator fun invoke(email: String, password: String) = repository.loginWithEmailAndPassword(email = email, password = password)
}
