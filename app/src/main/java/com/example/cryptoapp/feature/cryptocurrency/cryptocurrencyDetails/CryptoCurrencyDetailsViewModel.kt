package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.CryptoCurrencyDetails
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.CryptoCurrencyHistory
import com.example.cryptoapp.data.repository.CryptoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class CryptoCurrencyDetailsViewModel(private val repository: CryptoRepository) : ViewModel() {
    val cryptoCurrencyHistory = MutableStateFlow<Response<CryptoCurrencyHistory>?>(null)
    val cryptoCurrencyDetails = MutableStateFlow<Response<CryptoCurrencyDetails>?>(null)

    fun loadCryptoCurrencyDetails(uuid: String) {
        viewModelScope.launch {
            val response = repository.getCryptoCurrencyDetails(uuid = uuid)
            cryptoCurrencyDetails.value = response
        }
    }

    fun loadCryptoCurrencyHistory(uuid: String, timePeriod: String) {
        viewModelScope.launch {
            val response = repository.getCryptoCurrencyHistory(uuid = uuid, timePeriod = timePeriod)
            cryptoCurrencyHistory.value = response
        }
    }
}
