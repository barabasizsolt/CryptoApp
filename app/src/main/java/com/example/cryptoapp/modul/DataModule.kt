package com.example.cryptoapp.modul

import com.example.cryptoapp.data.NetworkManager
import com.example.cryptoapp.data.repository.CryptoRepository
import com.example.cryptoapp.data.repository.EventRepository
import com.example.cryptoapp.data.repository.ExchangeRepository
import org.koin.dsl.module

val dataModule = module {
    // Networking
    single { NetworkManager() }

    // Repository
    single { CryptoRepository(get()) }
    single { EventRepository(get()) }
    single { ExchangeRepository(get()) }
}
