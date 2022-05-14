package com.hackathon.auth.domain

import com.hackathon.auth.data.AuthenticationRepository

class GetCurrentUserUseCase(private val repository: AuthenticationRepository) {

    operator fun invoke() = repository.getCurrentUser()
}
