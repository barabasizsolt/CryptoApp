package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.constant.CryptoConstant
import com.example.cryptoapp.data.constant.CryptoConstant.DEFAULT_OFFSET
import com.example.cryptoapp.data.constant.CryptoConstant.toCryptoCurrencyUIModel
import com.example.cryptoapp.domain.cryptocurrency.GetCryptoCurrenciesUseCase
import com.example.cryptoapp.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CryptoCurrencyViewModel(private val getCryptoCurrencies: GetCryptoCurrenciesUseCase) : ViewModel() {
    private val _cryptoCurrencies = MutableStateFlow(emptyList<CryptoCurrencyUIModel>())
    val cryptoCurrencies: Flow<List<CryptoCurrencyUIModel>> = _cryptoCurrencies

    fun loadCryptoCurrencies(orderBy: String = CryptoConstant.MARKET_CAP_FIELD, orderDirection: String = CryptoConstant.DESC, offset: Int = CryptoConstant.DEFAULT_OFFSET, tags: Set<String> = setOf(), timePeriod: String = CryptoConstant.timePeriods[1]) {
        viewModelScope.launch {
            when (val result = getCryptoCurrencies(orderBy = orderBy, orderDirection = orderDirection, offset = offset, tags = tags, timePeriod = timePeriod)) {
                is Result.Success -> {
                    val cryptoCurrencyResults = result.data.map { currency ->
                        currency.toCryptoCurrencyUIModel(timePeriod)
                    } as MutableList
                    if (offset == DEFAULT_OFFSET) {
                        _cryptoCurrencies.value = cryptoCurrencyResults
                    } else {
                        _cryptoCurrencies.value = (_cryptoCurrencies.value + cryptoCurrencyResults) as MutableList<CryptoCurrencyUIModel>
                    }
                }
                is Result.Failure -> {
                    _cryptoCurrencies.value = mutableListOf()
                }
            }
        }
    }

    init {
        loadCryptoCurrencies()
    }
}
