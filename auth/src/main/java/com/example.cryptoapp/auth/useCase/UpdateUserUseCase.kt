package com.example.cryptoapp.auth.useCase

import android.net.Uri
import com.example.cryptoapp.auth.service.AuthenticationService
import kotlinx.coroutines.flow.filterNotNull

class UpdateUserUseCase(private val service: AuthenticationService) {

    operator fun invoke(photo: Uri?, userName: String) = service.updateUser(photo = photo, userName = userName).filterNotNull()
}
