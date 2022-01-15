package com.example.cryptoapp.domain

import com.example.cryptoapp.domain.cryptocurrency.GetCryptoCurrenciesUseCase
import com.example.cryptoapp.domain.cryptocurrency.GetCryptoCurrencyDetailsUseCase
import com.example.cryptoapp.domain.cryptocurrency.GetCryptoCurrencyHistoryUseCase
import com.example.cryptoapp.domain.exchange.GetExchangesUseCase
import com.example.cryptoapp.domain.news.GetNewsUseCase
import org.koin.dsl.module

val domainModule = module {
    // Use case
    factory { GetCryptoCurrenciesUseCase(repository = get()) }
    factory { GetCryptoCurrencyDetailsUseCase(repository = get()) }
    factory { GetCryptoCurrencyHistoryUseCase(repository = get()) }
    factory { GetNewsUseCase(repository = get()) }
    factory { GetExchangesUseCase(repository = get()) }
}
