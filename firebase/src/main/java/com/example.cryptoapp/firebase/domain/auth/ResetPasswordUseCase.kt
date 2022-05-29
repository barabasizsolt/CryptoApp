package com.example.cryptoapp.firebase.domain.auth

import com.example.cryptoapp.firebase.data.auth.AuthenticationRepository

class ResetPasswordUseCase(private val repository: AuthenticationRepository) {

    operator fun invoke(email: String) = repository.resetPassword(email = email)
}
