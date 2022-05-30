package com.example.cryptoapp.firebase.domain

import com.example.cryptoapp.firebase.data.AuthenticationRepository

class GetCurrentUserUseCase(private val repository: AuthenticationRepository) {

    operator fun invoke() = repository.getCurrentUser()
}
