package com.example.cryptoapp

import com.example.cryptoapp.data.repository.CryptoApiRepository
import com.example.cryptoapp.data.source.CryptoRetrofitInstance
import com.example.cryptoapp.feature.viewModel.CryptoApiViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cryptoCurrencyModule = module {
    single { CryptoRetrofitInstance() }
    factory { CryptoApiRepository(get()) }
    viewModel { CryptoApiViewModel(get()) }
}
