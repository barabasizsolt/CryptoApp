package com.barabasizsolt.feature

import com.barabasizsolt.feature.activity.MainActivityViewModel
import com.barabasizsolt.feature.screen.auth.login.LoginViewModel
import com.barabasizsolt.feature.screen.auth.register.SignUpViewModel
import com.barabasizsolt.feature.screen.main.category.CategoryViewModel
import com.barabasizsolt.feature.screen.main.cryptocurrency.cryptocurrencyDetails.CryptoCurrencyDetailsViewModel
import com.barabasizsolt.feature.screen.main.cryptocurrency.cryptocurrencyList.CryptoCurrencyViewModel
import com.barabasizsolt.feature.screen.main.exchange.exchangeDetail.ExchangeDetailViewModel
import com.barabasizsolt.feature.screen.main.exchange.exchangeList.ExchangeViewModel
import com.barabasizsolt.feature.screen.main.news.NewsViewModel
import com.barabasizsolt.feature.screen.main.user.ProfileViewModel
import com.barabasizsolt.feature.screen.main.watchlist.WatchListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel {
        MainActivityViewModel(getCurrentUserUseCase = get())
    }
    viewModel {
        LoginViewModel(
            loginWithEmailAndPassword = get(),
            getIntentForGoogleAccountLogin = get(),
            resetPasswordUseCase = get(),
            loginWithGoogleAccountUseCase = get()
        )
    }
    viewModel { SignUpViewModel(registerWithEmailAndPasswordUseCase = get()) }
    viewModel { CryptoCurrencyViewModel(useCase = get()) }
    viewModel { params ->
        CryptoCurrencyDetailsViewModel(
            uuid = params[0],
            detailsUseCase = get(),
            historyUseCase = get(),
            addCryptoCurrencyToWatchListUseCase = get(),
            deleteCryptoCurrencyFromWatchList = get(),
            isCryptoCurrencyAddedToWatchList = get(),
            getCurrentUserUseCase = get()
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
    viewModel {
        ProfileViewModel(
            getCurrentUserUseCase = get(),
            logOutUseCase = get(),
            updateUserUseCase = get()
        )
    }
    viewModel {
        WatchListViewModel(
            getCryptoCurrenciesForWatchList = get(),
            getCryptoCurrenciesInWatchList = get(),
            deleteCryptoCurrencyFromWatchList = get()
        )
    }
}