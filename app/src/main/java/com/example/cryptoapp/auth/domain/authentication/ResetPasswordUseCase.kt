package com.example.cryptoapp.auth.domain.authentication

import com.example.cryptoapp.auth.data.AuthenticationRepository

class ResetPasswordUseCase(private val repository: AuthenticationRepository) {

    operator fun invoke(email: String) = repository.resetPassword(email = email)
}
