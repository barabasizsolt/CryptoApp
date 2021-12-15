package com.example.cryptoapp.modul

import com.example.cryptoapp.domain.cryptocurrency.GetCryptoCurrenciesUseCase
import com.example.cryptoapp.domain.cryptocurrency.GetCryptoCurrencyDetailsUseCase
import com.example.cryptoapp.domain.cryptocurrency.GetCryptoCurrencyHistoryUseCase
import com.example.cryptoapp.domain.event.GetEventsUseCase
import com.example.cryptoapp.domain.exchange.GetExchangesUseCase
import org.koin.dsl.module

val domainModule = module {
    // Use case
    factory { GetCryptoCurrenciesUseCase(get()) }
    factory { GetCryptoCurrencyDetailsUseCase(get()) }
    factory { GetCryptoCurrencyHistoryUseCase(get()) }
    factory { GetEventsUseCase(get()) }
    factory { GetExchangesUseCase(get()) }
}
