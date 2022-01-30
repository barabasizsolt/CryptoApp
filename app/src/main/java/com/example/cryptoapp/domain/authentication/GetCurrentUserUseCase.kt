package com.example.cryptoapp.domain.authentication

import com.example.cryptoapp.data.repository.AuthenticationRepository
import com.example.cryptoapp.domain.resultOf

class GetCurrentUserUseCase(private val repository: AuthenticationRepository) {

    operator fun invoke() = resultOf {
        repository.getCurrentUser()
    }
}
