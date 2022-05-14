package com.hackathon.auth.di

import com.hackathon.auth.data.AuthenticationRepository
import com.hackathon.auth.data.AuthenticationSource
import com.hackathon.auth.domain.GetCurrentUserUseCase
import com.hackathon.auth.domain.LogOutUseCase
import com.hackathon.auth.domain.LoginWithEmailAndPasswordUseCase
import com.hackathon.auth.domain.RegisterWithEmailAndPasswordUseCase
import com.hackathon.auth.domain.ResetPasswordUseCase
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

