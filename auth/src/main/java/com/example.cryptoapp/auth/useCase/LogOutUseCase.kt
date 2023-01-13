package com.example.cryptoapp.auth.useCase

import com.example.cryptoapp.auth.service.AuthenticationService
import kotlinx.coroutines.flow.filterNotNull

class LogOutUseCase(private val service: AuthenticationService) {

    operator fun invoke() = service.logOut().filterNotNull()
}
