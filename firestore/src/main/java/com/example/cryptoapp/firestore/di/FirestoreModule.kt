package com.example.cryptoapp.firestore.di

import com.example.cryptoapp.firestore.data.FirestoreService
import com.example.cryptoapp.firestore.data.FirestoreServiceImpl
import com.example.cryptoapp.firestore.domain.AddCryptoCurrencyToWatchListUseCase
import com.example.cryptoapp.firestore.domain.DeleteCryptoCurrencyFromWatchList
import com.example.cryptoapp.firestore.domain.GetAllCryptoCurrencyUseCase
import com.example.cryptoapp.firestore.domain.GetCryptoCurrencyUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

fun fireStoreModule(): List<Module> = listOf(dataModule(), domainModule())

private fun dataModule() = module {
    single<FirestoreService> { FirestoreServiceImpl() }
}

private fun domainModule() = module {
    factory { AddCryptoCurrencyToWatchListUseCase(service = get()) }
    factory { DeleteCryptoCurrencyFromWatchList(service = get()) }
    factory { GetCryptoCurrencyUseCase(service = get()) }
    factory { GetAllCryptoCurrencyUseCase(service = get()) }
}