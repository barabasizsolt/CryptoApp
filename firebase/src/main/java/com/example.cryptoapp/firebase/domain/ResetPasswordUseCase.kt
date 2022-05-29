package com.example.cryptoapp.firebase.domain

import com.example.cryptoapp.firebase.data.AuthenticationRepository

class ResetPasswordUseCase(private val repository: AuthenticationRepository) {

    operator fun invoke(email: String) = repository.resetPassword(email = email)
}
