package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.CryptoCurrencyDetails
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.CryptoCurrencyHistory
import com.example.cryptoapp.data.repository.CryptoApiRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class CryptoCurrencyDetailsViewModel(private val repository: CryptoApiRepository) : ViewModel() {
    private val cryptoCurrencyHistory: MutableLiveData<Response<CryptoCurrencyHistory>> = MutableLiveData()
    private val cryptoCurrencyDetails: MutableLiveData<Response<CryptoCurrencyDetails>> = MutableLiveData()

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

    fun getCryptoCurrencyDetails() = cryptoCurrencyDetails

    fun getCryptoCurrencyHistory() = cryptoCurrencyHistory
}
