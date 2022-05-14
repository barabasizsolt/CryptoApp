package com.example.cryptoapp.auth.domain.authentication

import com.example.cryptoapp.auth.data.AuthenticationRepository

class LogOutUseCase(private val repository: AuthenticationRepository) {

    operator fun invoke() = repository.logOut()
}
