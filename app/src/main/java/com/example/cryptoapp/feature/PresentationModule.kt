package com.example.cryptoapp.feature

import com.example.cryptoapp.feature.auth.login.LoginViewModel
import com.example.cryptoapp.feature.auth.register.SignUpViewModel
import com.example.cryptoapp.feature.main.category.CategoryViewModel
import com.example.cryptoapp.feature.main.cryptocurrency.cryptocurrencyDetails.CryptoCurrencyDetailsViewModel
import com.example.cryptoapp.feature.main.cryptocurrency.cryptocurrencyList.CryptoCurrencyViewModel
import com.example.cryptoapp.feature.main.exchange.ExchangeViewModel
import com.example.cryptoapp.feature.main.news.NewsViewModel
import com.example.cryptoapp.feature.main.user.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    // View model
    viewModel { CryptoCurrencyViewModel(useCase = get()) }
    viewModel { params ->
        CryptoCurrencyDetailsViewModel(
            uuid = params[0],
            detailsUseCase = get(),
            historyUseCase = get()
        )
    }
    viewModel { CategoryViewModel(getCategoriesUseCase = get()) }
    viewModel { NewsViewModel(useCase = get()) }
    viewModel { ExchangeViewModel(useCase = get()) }
    viewModel {
        LoginViewModel(
            loginWithEmailAndPasswordUseCase = get(),
            resetPasswordUseCase = get()
        )
    }
    viewModel { SignUpViewModel(registerWithEmailAndPasswordUseCase = get()) }
    viewModel { ProfileViewModel(getCurrentUserUseCase = get()) }
}
