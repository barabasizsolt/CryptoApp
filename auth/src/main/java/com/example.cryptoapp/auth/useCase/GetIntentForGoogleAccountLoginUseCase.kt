package com.example.cryptoapp.auth.useCase

import android.app.Activity
import android.content.Context
import com.example.cryptoapp.auth.service.AuthenticationService

class GetIntentForGoogleAccountLoginUseCase(private val service: AuthenticationService) {

    operator fun invoke(context: Context) = service.getIntentForGoogleAccountLogin(context = context)
}