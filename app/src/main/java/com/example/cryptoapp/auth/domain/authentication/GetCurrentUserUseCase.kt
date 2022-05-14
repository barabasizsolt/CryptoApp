package com.example.cryptoapp.auth.domain.authentication

import com.example.cryptoapp.auth.data.AuthenticationRepository
import com.example.cryptoapp.domain.resultOf

class GetCurrentUserUseCase(private val repository: AuthenticationRepository) {

    operator fun invoke() = resultOf {
        repository.getCurrentUser()
    }
}
