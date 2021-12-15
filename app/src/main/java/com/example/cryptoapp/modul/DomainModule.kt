package com.example.cryptoapp.modul

import com.example.cryptoapp.domain.GetCryptoCurrenciesUseCase
import com.example.cryptoapp.domain.GetCryptoCurrencyDetailsUseCase
import com.example.cryptoapp.domain.GetCryptoCurrencyHistoryUseCase
import org.koin.dsl.module

val domainModule = module {
    // Use case
    factory { GetCryptoCurrenciesUseCase(get()) }
    factory { GetCryptoCurrencyDetailsUseCase(get()) }
    factory { GetCryptoCurrencyHistoryUseCase(get()) }
}
