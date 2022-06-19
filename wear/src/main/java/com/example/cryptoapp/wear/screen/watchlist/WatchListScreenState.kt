package com.example.cryptoapp.wear.screen.watchlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.cryptoapp.data.model.RefreshType
import com.example.cryptoapp.data.model.Result
import com.example.cryptoapp.data.model.cryptocurrency.CryptoCurrency
import com.example.cryptoapp.domain.useCase.cryptocurrency.GetCryptoCurrenciesForWatchListUseCase
import com.example.cryptoapp.firestore.useCase.DeleteCryptoCurrencyFromWatchList
import com.example.cryptoapp.firestore.useCase.GetCryptoCurrenciesInWatchListUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WatchListScreenState(
    private val stateScope: CoroutineScope,
    private val getCryptoCurrenciesForWatchList: GetCryptoCurrenciesForWatchListUseCase,
    private val getCryptoCurrenciesInWatchList: GetCryptoCurrenciesInWatchListUseCase,
    private val deleteCryptoCurrencyFromWatchList: DeleteCryptoCurrencyFromWatchList
) {

    var cryptoCurrencies by mutableStateOf<List<CryptoCurrency>>(value = emptyList())
        private set
    var action by mutableStateOf<Action?>(value = null)
        private set
    //var watchListSummary by mutableStateOf<WatchListSummaryUiModel?>(value = null)
    //    private set
    var screenState by mutableStateOf<ScreenState>(value = ScreenState.Loading)

    init {
        refreshData()
    }

    fun refreshData() {
        screenState = ScreenState.Loading
        stateScope.launch {
            getCryptoCurrenciesInWatchList().onEach { uuids ->
                if (uuids.isNotEmpty()) {
                    when (
                        val result = getCryptoCurrenciesForWatchList(
                            uuids = uuids,
                            refreshType = RefreshType.FORCE_REFRESH
                        )
                    ) {
                        is Result.Success -> {
                            cryptoCurrencies = result.data
                            //watchListSummary = result.data.toWatchListSummaryUiModel()
                            screenState = ScreenState.Normal
                        }
                        is Result.Failure -> {
                            screenState = ScreenState.Error(message = result.exception.message.orEmpty())
                        }
                    }
                } else {
                    cryptoCurrencies = emptyList()
                    screenState = ScreenState.Normal
                }
            }.stateIn(scope = this)
        }
    }

    fun deleteCryptoCurrency(uid: String) {
        deleteCryptoCurrencyFromWatchList(id = uid)
    }

    fun onItemClicked(id: String) {
        action = Action.OnItemClicked(id = id)
    }

    sealed class ScreenState {
        object Loading: ScreenState()
        object Normal: ScreenState()
        data class Error(val message: String): ScreenState()
    }

    sealed class Action {
        data class OnItemClicked(val id: String) : Action()
    }
}