package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.constant.CryptoConstant
import com.example.cryptoapp.data.model.cryptoCurrency.AllCryptoCurrencies
import com.example.cryptoapp.data.repository.CryptoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class CryptoCurrencyViewModel(private val repository: CryptoRepository) : ViewModel() {
    val cryptoCurrencies: MutableStateFlow<Response<AllCryptoCurrencies>?> = MutableStateFlow(null)

    fun loadCryptoCurrencies(orderBy: String = CryptoConstant.MARKET_CAP_FIELD, orderDirection: String = CryptoConstant.DESC, offset: Int = CryptoConstant.OFFSET, tags: Set<String> = setOf(), timePeriod: String = CryptoConstant.timePeriods[1]) {
        viewModelScope.launch {
            val response = repository.getAllCryptoCurrencies(orderBy = orderBy, orderDirection = orderDirection, offset = offset, tags = tags, timePeriod = timePeriod)
            cryptoCurrencies.value = response
        }
    }
}
