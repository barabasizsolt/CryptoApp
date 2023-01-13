package com.example.cryptoapp.data

import com.example.cryptoapp.data.repository.category.CategoryRepository
import com.example.cryptoapp.data.repository.exchange.ExchangeRepository
import com.example.cryptoapp.data.repository.news.NewsRepository
import com.example.cryptoapp.data.repository.cryptocurrency.CryptoCurrencyDetailsRepository
import com.example.cryptoapp.data.repository.cryptocurrency.CryptoCurrencyHistoryRepository
import com.example.cryptoapp.data.repository.cryptocurrency.CryptoCurrencyRepository
import com.example.cryptoapp.data.repository.cryptocurrency.CryptoCurrencyWatchListRepository
import com.example.cryptoapp.data.repository.exchange.ExchangeDetailRepository
import com.example.cryptoapp.data.repository.exchange.ExchangeHistoryRepository
import com.example.cryptoapp.data.retrofitInstance.CoinGekkoRetrofitInstance
import com.example.cryptoapp.data.retrofitInstance.CoinRankingRetrofitInstance
import com.example.cryptoapp.data.source.CategorySource
import com.example.cryptoapp.data.source.CryptoCurrencySource
import com.example.cryptoapp.data.source.ExchangeSource
import com.example.cryptoapp.data.source.NewsSource
import org.koin.core.module.Module
import org.koin.dsl.module

fun createDataModules(coinRankingUrl: String, coinGekkoUrl: String): List<Module> = listOf(
    createRetrofitInstances(coinRankingUrl = coinRankingUrl, coinGekkoUrl = coinGekkoUrl),
    createSources(),
    createRepositories()
)

private fun createRepositories() = module {
    single { CryptoCurrencyRepository(source = get()) }
    single { CryptoCurrencyWatchListRepository(source = get()) }
    single { CryptoCurrencyDetailsRepository(source = get()) }
    single { CryptoCurrencyHistoryRepository(source = get()) }
    single { CategoryRepository(source = get()) }
    single { ExchangeRepository(source = get()) }
    single { ExchangeDetailRepository(source = get()) }
    single { ExchangeHistoryRepository(source = get()) }
    single { NewsRepository(source = get()) }
}

private fun createSources() = module {
    single { CryptoCurrencySource(retrofitInstance = get()) }
    single { CategorySource(retrofitInstance = get()) }
    single { ExchangeSource(retrofitInstance = get()) }
    single { NewsSource(retrofitInstance = get()) }
}

private fun createRetrofitInstances(coinRankingUrl: String, coinGekkoUrl: String) = module {
    single { CoinRankingRetrofitInstance(baseUrl = coinRankingUrl) }
    single { CoinGekkoRetrofitInstance(baseUrl = coinGekkoUrl) }
}
