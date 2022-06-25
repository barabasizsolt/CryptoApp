package com.example.cryptoapp.firestore.di

import com.example.cryptoapp.firestore.service.FirestoreService
import com.example.cryptoapp.firestore.service.FirestoreServiceImpl
import com.example.cryptoapp.firestore.useCase.AddCryptoCurrencyToWatchListUseCase
import com.example.cryptoapp.firestore.useCase.DeleteCryptoCurrencyFromWatchListUseCase
import com.example.cryptoapp.firestore.useCase.GetCryptoCurrenciesInWatchListUseCase
import com.example.cryptoapp.firestore.useCase.IsCryptoCurrencyAddedToWatchList
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