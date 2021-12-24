package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.model.Result
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.details.CryptoCurrencyDetails
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.details.CryptoCurrencyDetailsUIModel
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.history.CryptoCurrencyHistory
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.history.SingleCryptoCurrencyHistoryResponse
import com.example.cryptoapp.domain.cryptocurrency.GetCryptoCurrencyDetailsUseCase
import com.example.cryptoapp.domain.cryptocurrency.GetCryptoCurrencyHistoryUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CryptoCurrencyDetailsViewModel(
    uuid: String,
    private val detailsUseCase: GetCryptoCurrencyDetailsUseCase,
    private val historyUseCase: GetCryptoCurrencyHistoryUseCase
) : ViewModel() {
    private val _cryptoCurrencyDetails = MutableStateFlow<CryptoCurrencyDetailsUIModel?>(null)
    val cryptoCurrencyDetails: Flow<CryptoCurrencyDetailsUIModel?> = _cryptoCurrencyDetails

    private val _cryptoCurrencyDetailsInfo = MutableStateFlow<CryptoCurrencyDetailsInfoUIModel?>(null)
    val cryptoCurrencyDetailsInfo: Flow<CryptoCurrencyDetailsInfoUIModel?> = _cryptoCurrencyDetailsInfo

    private val _cryptoCurrencyHistory = MutableStateFlow<CryptoCurrencyHistoryUIModel?>(null)
    val cryptoCurrencyHistory: Flow<CryptoCurrencyHistoryUIModel?> = _cryptoCurrencyHistory

    init {
        loadCryptoCurrencyDetails(uuid = uuid)
    }

    private fun loadCryptoCurrencyDetails(uuid: String) {
        viewModelScope.launch {
            when (val result = detailsUseCase(uuid = uuid)) {
                is Result.Success -> {
                    _cryptoCurrencyDetails.value = result.data.toCryptoCurrencyDetailsUIModel()
                    _cryptoCurrencyDetailsInfo.value = result.data.toCryptoCurrencyDetailsInfoUIModel()
                }
                is Result.Failure -> {
                }
            }
        }
    }

    fun loadCryptoCurrencyHistory(uuid: String, timePeriod: String) {
        viewModelScope.launch {
            when (val result = historyUseCase(uuid = uuid, timePeriod = timePeriod)) {
                is Result.Success -> {
                    _cryptoCurrencyHistory.value = result.data.toCryptoCurrencyHistoryUIModel()
                }
                is Result.Failure -> {
                }
            }
        }
    }

    private fun CryptoCurrencyDetails.toCryptoCurrencyDetailsUIModel() = CryptoCurrencyDetailsUIModel(
        uuid = uuid,
        symbol = symbol,
        name = name,
        iconUrl = iconUrl,
        marketCap = marketCap,
        price = price,
        change = change,
        volume = volume
    )

    private fun CryptoCurrencyDetails.toCryptoCurrencyDetailsInfoUIModel() = CryptoCurrencyDetailsInfoUIModel(
        rank = rank.toString(),
        totalSupply = totalSupply,
        circulating = circulating,
        btcPrice = btcPrice,
        allTimeHigh = allTimeHigh,
        description = description
    )

    private fun CryptoCurrencyHistory.toCryptoCurrencyHistoryUIModel() = CryptoCurrencyHistoryUIModel(
        history = history as MutableList<SingleCryptoCurrencyHistoryResponse>
    )
}
