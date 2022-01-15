package com.example.cryptoapp.data

import com.example.cryptoapp.data.repository.ExchangeRepository
import com.example.cryptoapp.data.repository.NewsRepository
import com.example.cryptoapp.data.repository.cryptocurrency.CryptoCurrencyDetailsRepository
import com.example.cryptoapp.data.repository.cryptocurrency.CryptoCurrencyHistoryRepository
import com.example.cryptoapp.data.repository.cryptocurrency.CryptoCurrencyRepository
import org.koin.dsl.module

val dataModule = module {
    // Networking
    single { NetworkManager() }

    // Repository
    single { CryptoCurrencyRepository(manager = get()) }
    single { CryptoCurrencyDetailsRepository(manager = get()) }
    single { CryptoCurrencyHistoryRepository(manager = get()) }
    single { NewsRepository(manager = get()) }
    single { ExchangeRepository(manager = get()) }
}
