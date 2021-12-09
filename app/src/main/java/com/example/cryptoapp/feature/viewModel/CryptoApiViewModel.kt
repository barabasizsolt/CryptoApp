package com.example.cryptoapp.feature.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.constant.CryptoConstant.DESC
import com.example.cryptoapp.data.constant.CryptoConstant.MARKET_CAP_FIELD
import com.example.cryptoapp.data.constant.CryptoConstant.OFFSET
import com.example.cryptoapp.data.constant.CryptoConstant.timePeriods
import com.example.cryptoapp.data.model.cryptoCurrency.AllCryptoCurrencies
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.CryptoCurrencyDetails
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.CryptoCurrencyHistory
import com.example.cryptoapp.data.repository.CryptoApiRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class CryptoApiViewModel(private val repository: CryptoApiRepository) : ViewModel() {
    val allCryptoCurrenciesResponse: MutableLiveData<Response<AllCryptoCurrencies>> = MutableLiveData()
    val cryptoCurrencyHistory: MutableLiveData<Response<CryptoCurrencyHistory>> = MutableLiveData()
    val cryptoCurrencyDetails: MutableLiveData<Response<CryptoCurrencyDetails>> = MutableLiveData()

    fun getAllCryptoCurrencies(orderBy: String = MARKET_CAP_FIELD, orderDirection: String = DESC, offset: Int = OFFSET, tags: Set<String> = setOf(), timePeriod: String = timePeriods[1]) {
        viewModelScope.launch {
            val response = repository.getAllCryptoCurrencies(orderBy = orderBy, orderDirection = orderDirection, offset = offset, tags = tags, timePeriod = timePeriod)
            allCryptoCurrenciesResponse.value = response
        }
    }

    fun getCryptoCurrencyDetails(uuid: String) {
        viewModelScope.launch {
            val response = repository.getCryptoCurrencyDetails(uuid = uuid)
            cryptoCurrencyDetails.value = response
        }
    }

    fun getCryptoCurrencyHistory(uuid: String, timePeriod: String) {
        viewModelScope.launch {
            val response = repository.getCryptoCurrencyHistory(uuid = uuid, timePeriod = timePeriod)
            cryptoCurrencyHistory.value = response
        }
    }
}
