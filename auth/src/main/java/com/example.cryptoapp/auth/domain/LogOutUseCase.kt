package com.hackathon.auth.domain

import com.hackathon.auth.data.AuthenticationRepository

class LogOutUseCase(private val repository: AuthenticationRepository) {

    operator fun invoke() = repository.logOut()
}
