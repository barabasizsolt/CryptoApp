package com.example.cryptoapp.domain.authentication

import com.example.cryptoapp.data.repository.AuthenticationRepository

class ResetPasswordUseCase(private val repository: AuthenticationRepository) {

    operator fun invoke(email: String) = repository.resetPassword(email = email)
}
