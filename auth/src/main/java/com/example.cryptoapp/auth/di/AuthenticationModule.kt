package com.example.cryptoapp.auth.di

import com.example.cryptoapp.auth.service.AuthenticationServiceImpl
import com.example.cryptoapp.auth.service.AuthenticationService
import com.example.cryptoapp.auth.useCase.GetCurrentUserUseCase
import com.example.cryptoapp.auth.useCase.LogOutUseCase
import com.example.cryptoapp.auth.useCase.LoginWithEmailAndPasswordUseCase
import com.example.cryptoapp.auth.useCase.RegisterWithEmailAndPasswordUseCase
import com.example.cryptoapp.auth.useCase.ResetPasswordUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

fun createAuthenticationModules(): List<Module> = listOf(createServiceModule(), createUseCaseModule())

private fun createServiceModule() = module {
    single<AuthenticationService>{ AuthenticationServiceImpl() }
}

private fun createUseCaseModule() = module {
    factory { LoginWithEmailAndPasswordUseCase(service = get()) }
    factory { LogOutUseCase(service = get()) }
    factory { RegisterWithEmailAndPasswordUseCase(service = get()) }
    factory { ResetPasswordUseCase(service = get()) }
    factory { GetCurrentUserUseCase(service = get()) }
}

