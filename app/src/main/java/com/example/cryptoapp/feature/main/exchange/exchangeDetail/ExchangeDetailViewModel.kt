package com.example.cryptoapp.feature.main.exchange.exchangeDetail

import android.util.Log
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

    init {
        refreshData()
        firstRefresh = false
    }

    fun refreshData() {
        viewModelScope.launch {
            listOf(
                async {
                    when (val result = getExchangeDetail(id = exchangeId)) {
                        is Result.Success -> {
                            exchangeDetails = result.data.toUiModel()
                            Log.d("details:", exchangeDetails.toString())
                        }
                        is Result.Failure -> {
                            Log.d("detailsError", result.exception.message.toString())
                            if (firstRefresh) {
                                // show card
                            } else {
                                // show snackBar
                            }
                        }
                    }
                },
                async {
                    when (val result = getExchangeHistory(id = exchangeId)) {
                        is Result.Success -> {
                            exchangeHistory = result.data.toUiModel()
                            Log.d("history:", exchangeHistory.toString())
                        }
                        is Result.Failure -> {
                            Log.d("historyError", result.exception.message.toString())
                            if (firstRefresh) {
                                // show card
                            } else {
                                // show snackBar
                            }
                        }
                    }
                }
            ).awaitAll()
        }
    }
}
