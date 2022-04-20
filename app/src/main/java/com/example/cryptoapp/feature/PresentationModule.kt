package com.example.cryptoapp.feature

import com.example.cryptoapp.feature.screen.auth.login.LoginViewModel
import com.example.cryptoapp.feature.screen.auth.register.SignUpViewModel
import com.example.cryptoapp.feature.screen.main.category.CategoryViewModel
import com.example.cryptoapp.feature.screen.main.cryptocurrency.cryptocurrencyDetails.CryptoCurrencyDetailsViewModel
import com.example.cryptoapp.feature.screen.main.cryptocurrency.cryptocurrencyList.CryptoCurrencyViewModel
import com.example.cryptoapp.feature.screen.main.exchange.exchangeDetail.ExchangeDetailViewModel
import com.example.cryptoapp.feature.screen.main.exchange.exchangeList.ExchangeViewModel
import com.example.cryptoapp.feature.screen.main.news.NewsViewModel
import com.example.cryptoapp.feature.screen.main.user.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel {
        LoginViewModel(
            loginWithEmailAndPasswordUseCase = get(),
            resetPasswordUseCase = get()
        )
    }
    viewModel { SignUpViewModel(registerWithEmailAndPasswordUseCase = get()) }
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
    viewModel { params ->
        ExchangeDetailViewModel(
            exchangeId = params[0],
            getExchangeDetails = get(),
            getExchangeHistory = get()
        )
    }
    viewModel { ProfileViewModel(getCurrentUserUseCase = get()) }
}
