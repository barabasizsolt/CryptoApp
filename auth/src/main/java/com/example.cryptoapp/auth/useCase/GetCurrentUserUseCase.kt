package com.example.cryptoapp.auth.useCase

import com.example.cryptoapp.auth.service.AuthenticationService

class GetCurrentUserUseCase(private val service: AuthenticationService) {

    operator fun invoke() = service.getCurrentUser()
}
