package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.constant.CryptoConstant.HOUR24
import com.example.cryptoapp.data.constant.CryptoConstant.toCryptoCurrencyDetailsInfoUIModel
import com.example.cryptoapp.data.constant.CryptoConstant.toCryptoCurrencyDetailsUIModel
import com.example.cryptoapp.data.constant.CryptoConstant.toCryptoCurrencyHistoryUIModel
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.details.CryptoCurrencyDetailsInfoUIModel
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.details.CryptoCurrencyDetailsUIModel
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.history.CryptoCurrencyHistoryUIModel
import com.example.cryptoapp.domain.GetCryptoCurrencyDetailsUseCase
import com.example.cryptoapp.domain.GetCryptoCurrencyHistoryUseCase
import com.example.cryptoapp.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CryptoCurrencyDetailsViewModel(
    uuid: String,
    timePeriod: String = HOUR24,
    private val detailsUseCase: GetCryptoCurrencyDetailsUseCase,
    private val historyUseCase: GetCryptoCurrencyHistoryUseCase
    ) : ViewModel() {
    val cryptoCurrencyDetails = MutableStateFlow<CryptoCurrencyDetailsUIModel?>(null)
    val cryptoCurrencyDetailsInfo = MutableStateFlow<CryptoCurrencyDetailsInfoUIModel?>(null)
    val cryptoCurrencyHistory = MutableStateFlow<CryptoCurrencyHistoryUIModel?>(null)

    private fun loadCryptoCurrencyDetails(uuid: String) {
        viewModelScope.launch {
            when(val result = detailsUseCase(uuid = uuid)){
                is Result.Success -> {
                    cryptoCurrencyDetails.value = result.data.toCryptoCurrencyDetailsUIModel()
                    cryptoCurrencyDetailsInfo.value = result.data.toCryptoCurrencyDetailsInfoUIModel()
                }
                is Result.Failure -> {

                }
            }
        }
    }

    fun loadCryptoCurrencyHistory(uuid: String, timePeriod: String) {
        viewModelScope.launch {
            when(val result = historyUseCase(uuid = uuid, timePeriod = timePeriod)){
                is Result.Success -> {
                    cryptoCurrencyHistory.value = result.data.toCryptoCurrencyHistoryUIModel()
                }
                is Result.Failure -> {

                }
            }
        }
    }

    init {
        //loadCryptoCurrencyHistory(uuid = uuid, timePeriod = timePeriod)
        loadCryptoCurrencyDetails(uuid = uuid)
    }
}
