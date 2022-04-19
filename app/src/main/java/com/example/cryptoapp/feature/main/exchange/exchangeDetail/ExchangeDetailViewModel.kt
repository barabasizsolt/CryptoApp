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
import com.example.cryptoapp.feature.shared.utils.pushEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ExchangeDetailViewModel (
    private val exchangeId: String,
    private val getExchangeDetail: GetExchangeDetailsUseCase,
    private val getExchangeHistory: GetExchangeHistoryUseCase,
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    var exchangeDetails by mutableStateOf<ExchangeDetailUiModel.ExchangeDetail?>(value = null)
        private set
    var exchangeHistory by mutableStateOf<ExchangeDetailUiModel.ExchangeDetailHistory?>(value = null)
        private set
    private var firstLoad: Boolean = true

    var screenState by mutableStateOf<ScreenState?>(value = null)

    private var selectedChip: Int = 0
    private val exchangeTimePeriods = listOf("24h", "7d")
    private val timePeriods: List<Int> = listOf(1, 7)

    init {
        refreshData()
    }

    fun refreshData() {
        if(!_isRefreshing.value) {
            _isRefreshing.value = true
            viewModelScope.launch(Dispatchers.Default) {
                when (val result = getExchangeDetail(id = exchangeId)) {
                    is Result.Success -> {
                        screenState = ScreenState.Normal
                        exchangeDetails = result.data.toUiModel()
                    }
                    is Result.Failure -> showError(message = result.exception.message.toString())
                }
                getExchangeHistory()
            }
            _isRefreshing.value = false
            if (firstLoad) {
                firstLoad = false
            }
        }
    }

    private suspend fun getExchangeHistory(){
        when (val result = getExchangeHistory(id = exchangeId, days = timePeriods[selectedChip])) {
            is Result.Success -> exchangeHistory = result.data.toUiModel(timePeriod = exchangeTimePeriods[selectedChip])
            is Result.Failure -> showError(message = result.exception.message.toString())
        }
    }

    fun onChipClicked(index: Int) {
        selectedChip = index
        refreshExchangeHistory()
    }

    fun clearContent() {
        viewModelScope.coroutineContext.cancelChildren()
    }

    private fun refreshExchangeHistory() {
        viewModelScope.launch { getExchangeHistory() }
    }

    private fun showError(message: String) {
        _isRefreshing.pushEvent(event = false)
        viewModelScope.coroutineContext.cancelChildren()
        if (firstLoad)
            screenState = ScreenState.ShowFirstErrorMessage else ScreenState.ShowSnackBarError(message = message)
    }

    sealed class ScreenState {

        object Loading: ScreenState()

        object Normal: ScreenState()

        data class ShowSnackBarError(val message: String) : ScreenState()

        object ShowFirstErrorMessage : ScreenState()
    }
}
