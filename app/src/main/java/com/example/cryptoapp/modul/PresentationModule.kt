package com.example.cryptoapp.modul

import com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails.CryptoCurrencyDetailsViewModel
import com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyList.CryptoCurrencyViewModel
import com.example.cryptoapp.feature.event.EventViewModel
import com.example.cryptoapp.feature.exchange.ExchangeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    // View model
    viewModel { CryptoCurrencyViewModel(get()) }
    viewModel { CryptoCurrencyDetailsViewModel(get(), get(), get(), get()) }
    viewModel { EventViewModel(get()) }
    viewModel { ExchangeViewModel(get()) }
}
