package com.example.cryptoapp.firebase.di

import com.example.cryptoapp.firebase.data.AuthenticationRepository
import com.example.cryptoapp.firebase.data.AuthenticationSource
import com.example.cryptoapp.firebase.domain.GetCurrentUserUseCase
import com.example.cryptoapp.firebase.domain.LogOutUseCase
import com.example.cryptoapp.firebase.domain.LoginWithEmailAndPasswordUseCase
import com.example.cryptoapp.firebase.domain.RegisterWithEmailAndPasswordUseCase
import com.example.cryptoapp.firebase.domain.ResetPasswordUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

fun createAuthenticationModules(): List<Module> = listOf(
    createAuthenticationDataModule(),
    createAuthenticationDomainModule()
)

private fun createAuthenticationDataModule() = module {
    single { AuthenticationSource() }
    single { AuthenticationRepository(source = get()) }
}

private fun createAuthenticationDomainModule() = module {
    factory { LoginWithEmailAndPasswordUseCase(repository = get()) }
    factory { LogOutUseCase(repository = get()) }
    factory { RegisterWithEmailAndPasswordUseCase(repository = get()) }
    factory { ResetPasswordUseCase(repository = get()) }
    factory { GetCurrentUserUseCase(repository = get()) }
}

