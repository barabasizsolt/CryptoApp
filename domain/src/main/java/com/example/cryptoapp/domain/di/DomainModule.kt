package com.example.cryptoapp.domain

import com.example.cryptoapp.domain.useCase.category.GetCategoriesUseCase
import com.example.cryptoapp.domain.useCase.cryptocurrency.GetCryptoCurrenciesUseCase
import com.example.cryptoapp.domain.useCase.cryptocurrency.GetCryptoCurrencyDetailsUseCase
import com.example.cryptoapp.domain.useCase.cryptocurrency.GetCryptoCurrencyHistoryUseCase
import com.example.cryptoapp.domain.useCase.exchange.GetExchangeDetailsUseCase
import com.example.cryptoapp.domain.useCase.exchange.GetExchangeHistoryUseCase
import com.example.cryptoapp.domain.useCase.exchange.GetExchangesUseCase
import com.example.cryptoapp.domain.useCase.news.GetNewsUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetCryptoCurrenciesUseCase(repository = get()) }
    factory { GetCryptoCurrencyDetailsUseCase(repository = get()) }
    factory { GetCryptoCurrencyHistoryUseCase(repository = get()) }
    factory { GetNewsUseCase(repository = get()) }
    factory { GetExchangesUseCase(repository = get()) }
    factory { GetExchangeDetailsUseCase(repository = get()) }
    factory { GetExchangeHistoryUseCase(repository = get()) }
    factory { GetCategoriesUseCase(repository = get()) }
}
