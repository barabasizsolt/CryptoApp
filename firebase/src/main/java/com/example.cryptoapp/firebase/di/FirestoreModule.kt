package com.example.cryptoapp.firebase.di

import com.example.cryptoapp.firebase.data.firestore.FirestoreRepository
import com.example.cryptoapp.firebase.data.firestore.FirestoreSource
import com.example.cryptoapp.firebase.domain.firestore.AddCryptoCurrencyToWatchListUseCase
import com.example.cryptoapp.firebase.domain.firestore.DeleteCryptoCurrencyFromWatchList
import com.example.cryptoapp.firebase.domain.firestore.GetAllCryptoCurrencyUseCase
import com.example.cryptoapp.firebase.domain.firestore.GetCryptoCurrencyUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

fun createFireStoreModules(): List<Module> = listOf(
    createFirestoreDataModule(),
    createFirestoreDomainModule()
)

private fun createFirestoreDataModule() = module {
    single { FirestoreSource() }
    single { FirestoreRepository(source = get()) }
}

private fun createFirestoreDomainModule() = module {
    factory { AddCryptoCurrencyToWatchListUseCase(repository = get()) }
    factory { DeleteCryptoCurrencyFromWatchList(repository = get()) }
    factory { GetCryptoCurrencyUseCase(repository = get()) }
    factory { GetAllCryptoCurrencyUseCase(repository = get()) }
}