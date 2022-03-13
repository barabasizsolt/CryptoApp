package com.example.cryptoapp.data

import com.example.cryptoapp.data.manager.AuthenticationManager
import com.example.cryptoapp.data.manager.NetworkManager
import com.example.cryptoapp.data.repository.AuthenticationRepository
import com.example.cryptoapp.data.repository.CategoryRepository
import com.example.cryptoapp.data.repository.ExchangeRepository
import com.example.cryptoapp.data.repository.NewsRepository
import com.example.cryptoapp.data.repository.cryptocurrency.CryptoCurrencyDetailsRepository
import com.example.cryptoapp.data.repository.cryptocurrency.CryptoCurrencyHistoryRepository
import com.example.cryptoapp.data.repository.cryptocurrency.CryptoCurrencyRepository
import org.koin.dsl.module

val dataModule = module {
    // Authentication
    single { AuthenticationRepository(manager = get()) }

    // Networking
    single { NetworkManager() }
    single { AuthenticationManager() }

    // Repository
    single { CryptoCurrencyRepository(manager = get()) }
    single { CryptoCurrencyDetailsRepository(manager = get()) }
    single { CryptoCurrencyHistoryRepository(manager = get()) }
    single { CategoryRepository(manager = get()) }
    single { ExchangeRepository(manager = get()) }
    single { NewsRepository(manager = get()) }
}
