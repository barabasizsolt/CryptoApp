package com.example.cryptoapp.feature.screen.main.exchange.exchangeDetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.domain.useCase.exchange.GetExchangeDetailsUseCase
import kotlinx.coroutines.launch
import com.example.cryptoapp.data.model.Result
import com.example.cryptoapp.domain.useCase.exchange.GetExchangeHistoryUseCase
import com.example.cryptoapp.feature.screen.main.cryptocurrency.cryptocurrencyDetails.helpers.UnitOfTimeType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

class ExchangeDetailViewModel (
    private val exchangeId: String,
    private val getExchangeDetails: GetExchangeDetailsUseCase,
    private val getExchangeHistory: GetExchangeHistoryUseCase,
) : ViewModel() {

    var exchangeDetails by mutableStateOf<ExchangeDetailUiModel.ExchangeDetail?>(value = null)
        private set
    var exchangeHistory by mutableStateOf<ExchangeDetailUiModel.ExchangeDetailHistory?>(value = null)
        private set
    var screenState by mutableStateOf<ScreenState>(value = ScreenState.Loading)

    private var selectedChip: Int = 0
    private val exchangeTimePeriods = listOf("24h", "7d")
    private val timePeriods: List<Int> = listOf(1, 7)
    private val unitOfTypes: List<UnitOfTimeType> = listOf(UnitOfTimeType.UNIT_24H, UnitOfTimeType.UNIT_7D)
    private var firstLoading: Boolean = true
    var unitOfTimeType: UnitOfTimeType = UnitOfTimeType.UNIT_24H

    init {
        refreshData()
        firstLoading = false
    }

    fun refreshData() {
        screenState = ScreenState.Loading
        viewModelScope.launch(Dispatchers.Default) {
            listOf(
                async { getExchangeDetails() },
                async { getExchangeHistory() }
            ).awaitAll()
        }
    }

    private suspend fun getExchangeDetails() {
        when (val result = getExchangeDetails(id = exchangeId)) {
            is Result.Success -> {
                exchangeDetails = result.data.toUiModel()
                screenState = ScreenState.Normal
            }
            is Result.Failure -> showError(message = result.exception.message.toString())
        }
    }

    private suspend fun getExchangeHistory(){
        when (val result = getExchangeHistory(id = exchangeId, days = timePeriods[selectedChip])) {
            is Result.Success -> {
                exchangeHistory = result.data.toUiModel(timePeriod = exchangeTimePeriods[selectedChip])
                screenState = ScreenState.Normal
            }
            is Result.Failure -> showError(message = result.exception.message.toString())
        }
    }

    fun onChipClicked(index: Int) {
        selectedChip = index
        screenState = ScreenState.Loading
        unitOfTimeType = unitOfTypes[index]
        viewModelScope.launch {
            getExchangeHistory()
        }
    }

    private fun showError(message: String) {
        screenState = if (firstLoading) ScreenState.ShowFirstLoadingError else ScreenState.ShowSnackBarError(message = message)
    }

    sealed class ScreenState {

        object Loading: ScreenState()

        object Normal: ScreenState()

        data class ShowSnackBarError(val message: String) : ScreenState()

        object ShowFirstLoadingError : ScreenState()
    }
}
