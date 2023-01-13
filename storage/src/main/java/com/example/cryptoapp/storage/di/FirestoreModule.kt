package com.example.cryptoapp.storage.di

import com.example.cryptoapp.storage.service.FirestoreService
import com.example.cryptoapp.storage.service.FirestoreServiceImpl
import com.example.cryptoapp.storage.useCase.AddCryptoCurrencyToWatchListUseCase
import com.example.cryptoapp.storage.useCase.DeleteCryptoCurrencyFromWatchListUseCase
import com.example.cryptoapp.storage.useCase.GetCryptoCurrenciesInWatchListUseCase
import com.example.cryptoapp.storage.useCase.IsCryptoCurrencyAddedToWatchList
import org.koin.core.module.Module
import org.koin.dsl.module

fun createFirestoreModule(): List<Module> = listOf(createServiceModule(), createUseCaseModule())

private fun createServiceModule() = module {
    single<FirestoreService> { FirestoreServiceImpl(authService = get()) }
}

private fun createUseCaseModule() = module {
    factory { AddCryptoCurrencyToWatchListUseCase(service = get()) }
    factory { DeleteCryptoCurrencyFromWatchListUseCase(service = get()) }
    factory { IsCryptoCurrencyAddedToWatchList(service = get()) }
    factory { GetCryptoCurrenciesInWatchListUseCase(service = get()) }
}