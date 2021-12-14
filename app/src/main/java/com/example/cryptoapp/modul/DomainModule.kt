package com.example.cryptoapp.modul

import com.example.cryptoapp.domain.GetCryptoCurrenciesUseCase
import org.koin.dsl.module

val domainModule = module {
    //Use case
    factory { GetCryptoCurrenciesUseCase(get()) }
}