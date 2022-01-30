package com.example.cryptoapp.domain.authentication

import com.example.cryptoapp.data.repository.AuthenticationRepository

class LogOutUseCase(private val repository: AuthenticationRepository) {

    operator fun invoke() = repository.logOut()
}
