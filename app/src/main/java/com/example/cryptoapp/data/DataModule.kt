package com.example.cryptoapp.data

import com.example.cryptoapp.data.source.AuthenticationSource
import com.example.cryptoapp.data.repository.AuthenticationRepository
import com.example.cryptoapp.data.repository.CategoryRepository
import com.example.cryptoapp.data.repository.ExchangeRepository
import com.example.cryptoapp.data.repository.NewsRepository
import com.example.cryptoapp.data.repository.cryptocurrency.CryptoCurrencyDetailsRepository
import com.example.cryptoapp.data.repository.cryptocurrency.CryptoCurrencyHistoryRepository
import com.example.cryptoapp.data.repository.cryptocurrency.CryptoCurrencyRepository
import com.example.cryptoapp.data.source.CategorySource
import com.example.cryptoapp.data.source.CryptoCurrencySource
import com.example.cryptoapp.data.source.ExchangeSource
import com.example.cryptoapp.data.source.NewsSource
import com.example.cryptoapp.data.retrofitInstance.CoinGekkoRetrofitInstance
import com.example.cryptoapp.data.retrofitInstance.CoinRankingRetrofitInstance
import org.koin.core.module.Module
import org.koin.dsl.module

fun createDataModules() : List<Module> =
    listOf(createAuthenticationModule())
        .plus(createContentModules())

private fun createAuthenticationModule() = module {
    single { AuthenticationSource() }
    single { AuthenticationRepository(source = get()) }
}

private fun createContentModules() : List<Module> = listOf(
    createRetrofitInstances(),
    createSources(),
    createRepositories()
)

private fun createRepositories() = module {
    single { CryptoCurrencyRepository(source = get()) }
    single { CryptoCurrencyDetailsRepository(source = get()) }
    single { CryptoCurrencyHistoryRepository(source = get()) }
    single { CategoryRepository(source = get()) }
    single { ExchangeRepository(source = get()) }
    single { NewsRepository(source = get()) }
}

private fun createSources() = module {
    single { CryptoCurrencySource(retrofitInstance = get()) }
    single { CategorySource(retrofitInstance = get()) }
    single { ExchangeSource(retrofitInstance = get()) }
    single { NewsSource(retrofitInstance = get()) }
}

private fun createRetrofitInstances() = module {
    single { CoinRankingRetrofitInstance() }
    single { CoinGekkoRetrofitInstance() }
}
