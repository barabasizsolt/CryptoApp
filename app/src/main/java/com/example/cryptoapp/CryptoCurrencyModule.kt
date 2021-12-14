package com.example.cryptoapp

import com.example.cryptoapp.data.repository.EventRepository
import com.example.cryptoapp.data.repository.CryptoRepository
import com.example.cryptoapp.data.repository.ExchangeRepository
import com.example.cryptoapp.data.NetworkManager
import com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails.CryptoCurrencyDetailsViewModel
import com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyList.CryptoCurrencyViewModel
import com.example.cryptoapp.feature.event.EventViewModel
import com.example.cryptoapp.feature.exchange.ExchangeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cryptoCurrencyModule = module {
    single { NetworkManager() }

    single { CryptoRepository(get()) }
    viewModel { CryptoCurrencyViewModel(get()) }
    viewModel { CryptoCurrencyDetailsViewModel(get()) }

    single { EventRepository(get()) }
    viewModel { EventViewModel(get()) }

    single { ExchangeRepository(get()) }
    viewModel { ExchangeViewModel(get()) }
}
