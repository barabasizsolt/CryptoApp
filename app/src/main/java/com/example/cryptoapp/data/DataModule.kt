package com.example.cryptoapp.data

import com.example.cryptoapp.data.repository.CryptoRepository
import com.example.cryptoapp.data.repository.ExchangeRepository
import com.example.cryptoapp.data.repository.NewsRepository
import org.koin.dsl.module

val dataModule = module {
    // Networking
    single { NetworkManager() }

    // Repository
    single { CryptoRepository(get()) }
    single { NewsRepository(get()) }
    single { ExchangeRepository(get()) }
}
