package com.example.cryptoapp.firestore.di

import com.example.cryptoapp.firestore.service.FirestoreService
import com.example.cryptoapp.firestore.service.FirestoreServiceImpl
import com.example.cryptoapp.firestore.useCase.AddCryptoCurrencyToWatchListUseCase
import com.example.cryptoapp.firestore.useCase.DeleteCryptoCurrencyFromWatchList
import com.example.cryptoapp.firestore.useCase.GetAllCryptoCurrencyUseCase
import com.example.cryptoapp.firestore.useCase.GetCryptoCurrencyUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

fun createFirestoreModule(): List<Module> = listOf(createServiceModule(), createUseCaseModule())

private fun createServiceModule() = module {
    single<FirestoreService> { FirestoreServiceImpl() }
}

private fun createUseCaseModule() = module {
    factory { AddCryptoCurrencyToWatchListUseCase(service = get()) }
    factory { DeleteCryptoCurrencyFromWatchList(service = get()) }
    factory { GetCryptoCurrencyUseCase(service = get()) }
    factory { GetAllCryptoCurrencyUseCase(service = get()) }
}