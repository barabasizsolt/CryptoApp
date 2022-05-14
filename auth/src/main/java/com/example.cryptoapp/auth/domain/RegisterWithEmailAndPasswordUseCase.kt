package com.hackathon.auth.domain

import com.hackathon.auth.data.AuthenticationRepository

class RegisterWithEmailAndPasswordUseCase(private val repository: AuthenticationRepository) {

    operator fun invoke(email: String, password: String) = repository.registerWithEmailAndPassWord(email = email, password = password)
}
