package com.example.cryptoapp

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainActivityModule = module {
    viewModel { MainActivityViewModel(getCurrentUserUseCase = get()) }
}
