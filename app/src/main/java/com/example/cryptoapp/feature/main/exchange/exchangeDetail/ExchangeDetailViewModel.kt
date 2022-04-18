package com.example.cryptoapp.feature.main.exchange.exchangeDetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.domain.exchange.GetExchangeDetailsUseCase
import kotlinx.coroutines.launch
import com.example.cryptoapp.data.model.Result
import com.example.cryptoapp.domain.exchange.GetExchangeHistoryUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.isActive

class ExchangeDetailViewModel (
    private val exchangeId: String,
    private val getExchangeDetail: GetExchangeDetailsUseCase,
    private val getExchangeHistory: GetExchangeHistoryUseCase,
) : ViewModel() {

    var exchangeDetails by mutableStateOf<ExchangeDetailUiModel.ExchangeDetail?>(value = null)
        private set
    var exchangeHistory by mutableStateOf<ExchangeDetailUiModel.ExchangeDetailHistory?>(value = null)
        private set
    var firstRefresh: Boolean = true

    var screenState by mutableStateOf<ScreenState?>(value = null)
    var event by mutableStateOf<Event?>(value = null)

    private var selectedChip: Int = 0
    private val exchangeTimePeriods = listOf("24h", "7d")
    private val timePeriods: List<Int> = listOf(1, 7)

    init {
        refreshData()
        firstRefresh = false
    }

    fun refreshData() {
        viewModelScope.launch {
            screenState = ScreenState.Loading
            listOf(
                async {
                    when (val result = getExchangeDetail(id = exchangeId)) {
                        is Result.Success -> exchangeDetails = result.data.toUiModel()
                        is Result.Failure -> showError(message = result.exception.message.toString())
                    }
                },
                async {
                    when (val result = getExchangeHistory(id = exchangeId, days = timePeriods[selectedChip])) {
                        is Result.Success -> exchangeHistory = result.data.toUiModel(timePeriod = exchangeTimePeriods[selectedChip])
                        is Result.Failure -> showError(message = result.exception.message.toString())
                    }
                }
            ).awaitAll()
            if (viewModelScope.coroutineContext.isActive) {
                screenState = ScreenState.Normal
            }
        }
    }

    fun onChipClicked(index: Int) {
        selectedChip = index
        refreshExchangeHistory()
    }



    private fun refreshExchangeHistory() {
        viewModelScope.launch {
            when (val result = getExchangeHistory(id = exchangeId, days = timePeriods[selectedChip])) {
                is Result.Success -> exchangeHistory = result.data.toUiModel(timePeriod = exchangeTimePeriods[selectedChip])
                is Result.Failure -> showError(message = result.exception.message.toString())
            }
        }
    }

    private fun showError(message: String) {
        viewModelScope.coroutineContext.cancelChildren()
        if (firstRefresh)
            event = Event.ShowFirstErrorMessage else Event.ShowSnackBarError(message = message)
    }

    sealed class ScreenState {

        object Loading: ScreenState()

        object Normal: ScreenState()
    }

    sealed class Event {

        data class ShowSnackBarError(val message: String) : Event()

        object ShowFirstErrorMessage : Event()
    }
}
