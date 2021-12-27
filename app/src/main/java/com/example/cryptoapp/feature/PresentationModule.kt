package com.example.cryptoapp.feature

import com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails.CryptoCurrencyDetailsViewModel
import com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyList.CryptoCurrencyViewModel
import com.example.cryptoapp.feature.exchange.ExchangeViewModel
import com.example.cryptoapp.feature.news.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    // View model
    viewModel { CryptoCurrencyViewModel(get()) }
    // viewModel { CryptoCurrencyDetailsViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { params ->
        CryptoCurrencyDetailsViewModel(
            uuid = params[0],
            chartBackgroundColor = params[1],
            chartTextColor = params[2],
            chartColor = params[3],
            detailsUseCase = get(),
            historyUseCase = get()
        )
    }
    viewModel { NewsViewModel(get()) }
    viewModel { ExchangeViewModel(get()) }
}
