package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.constant.CryptoConstant
import com.example.cryptoapp.data.constant.CryptoConstant.DEFAULT_OFFSET
import com.example.cryptoapp.data.constant.CryptoConstant.toCryptoCurrencyUIModel
import com.example.cryptoapp.data.model.cryptoCurrency.CryptoCurrencyUIModel
import com.example.cryptoapp.domain.cryptocurrency.GetCryptoCurrenciesUseCase
import com.example.cryptoapp.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CryptoCurrencyViewModel(private val getCryptoCurrencies: GetCryptoCurrenciesUseCase) : ViewModel() {
    val cryptoCurrencies = MutableStateFlow<MutableList<CryptoCurrencyUIModel>>(mutableListOf())

    fun loadCryptoCurrencies(orderBy: String = CryptoConstant.MARKET_CAP_FIELD, orderDirection: String = CryptoConstant.DESC, offset: Int = CryptoConstant.DEFAULT_OFFSET, tags: Set<String> = setOf(), timePeriod: String = CryptoConstant.timePeriods[1]) {
        viewModelScope.launch {
            when (val result = getCryptoCurrencies(orderBy = orderBy, orderDirection = orderDirection, offset = offset, tags = tags, timePeriod = timePeriod)) {
                is Result.Success -> {
                    val cryptoCurrencyResults = result.data.map { currency ->
                        currency.toCryptoCurrencyUIModel(timePeriod)
                    } as MutableList
                    if (offset == DEFAULT_OFFSET) {
                        cryptoCurrencies.value = cryptoCurrencyResults
                    } else {
                        cryptoCurrencies.value = (cryptoCurrencies.value + cryptoCurrencyResults) as MutableList<CryptoCurrencyUIModel>
                    }
                }
                is Result.Failure -> {
                    cryptoCurrencies.value = mutableListOf()
                }
            }
        }
    }

    init {
        loadCryptoCurrencies()
    }
}
