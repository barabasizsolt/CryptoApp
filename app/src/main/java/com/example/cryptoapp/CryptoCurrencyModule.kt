package com.example.cryptoapp

import com.example.cryptoapp.data.repository.CoinGekkoApiRepository
import com.example.cryptoapp.data.repository.CryptoApiRepository
import com.example.cryptoapp.data.source.CoinGekkoRetrofitInstance
import com.example.cryptoapp.data.source.CryptoRetrofitInstance
import com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails.CryptoCurrencyDetailsViewModel
import com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyList.CryptoCurrencyViewModel
import com.example.cryptoapp.feature.event.EventViewModel
import com.example.cryptoapp.feature.exchange.ExchangeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cryptoCurrencyModule = module {
    single { CryptoRetrofitInstance() }
    single { CryptoApiRepository(get()) }
    viewModel { CryptoCurrencyViewModel(get()) }
    viewModel { CryptoCurrencyDetailsViewModel(get()) }

    single { CoinGekkoRetrofitInstance() }
    single { CoinGekkoApiRepository(get()) }
    viewModel { ExchangeViewModel(get()) }
    viewModel { EventViewModel(get()) }
}
