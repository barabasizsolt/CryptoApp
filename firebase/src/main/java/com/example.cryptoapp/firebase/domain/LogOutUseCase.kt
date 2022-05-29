package com.example.cryptoapp.firebase.domain

import com.example.cryptoapp.firebase.data.AuthenticationRepository

class LogOutUseCase(private val repository: AuthenticationRepository) {

    operator fun invoke() = repository.logOut()
}
