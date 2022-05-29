package com.example.cryptoapp.firebase.domain.auth

import com.example.cryptoapp.firebase.data.auth.AuthenticationRepository

class GetCurrentUserUseCase(private val repository: AuthenticationRepository) {

    operator fun invoke() = repository.getCurrentUser()
}
