package com.example.cryptoapp.wear.screen.main.watchlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.cryptoapp.auth.AuthResult
import com.example.cryptoapp.auth.useCase.LogOutUseCase
import com.example.cryptoapp.data.model.RefreshType
import com.example.cryptoapp.data.model.Result
import com.example.cryptoapp.domain.useCase.cryptocurrency.GetCryptoCurrenciesForWatchListUseCase
import com.example.cryptoapp.storage.useCase.GetCryptoCurrenciesInWatchListUseCase
import com.example.cryptoapp.wear.common.Event
import com.example.cryptoapp.wear.screen.main.CryptoCurrencyUiModel
import com.example.cryptoapp.wear.screen.main.toUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@Composable
fun rememberWatchListScreenState(
    stateScope: CoroutineScope = rememberCoroutineScope(),
    getCryptoCurrenciesForWatchList: GetCryptoCurrenciesForWatchListUseCase = get(),
    getCryptoCurrenciesInWatchList: GetCryptoCurrenciesInWatchListUseCase = get(),
    logOut: LogOutUseCase = get()
) : WatchListScreenState = rememberSaveable(
    saver = WatchListScreenState.getSaver(
        stateScope = stateScope,
        getCryptoCurrenciesInWatchList = getCryptoCurrenciesInWatchList,
        getCryptoCurrenciesForWatchList = getCryptoCurrenciesForWatchList,
        logOut = logOut
    )
) {
    WatchListScreenState(
        stateScope = stateScope,
        getCryptoCurrenciesInWatchList = getCryptoCurrenciesInWatchList,
        getCryptoCurrenciesForWatchList = getCryptoCurrenciesForWatchList,
        logOut = logOut
    )
}

class WatchListScreenState(
    private val stateScope: CoroutineScope,
    private val getCryptoCurrenciesForWatchList: GetCryptoCurrenciesForWatchListUseCase,
    private val getCryptoCurrenciesInWatchList: GetCryptoCurrenciesInWatchListUseCase,
    private val logOut: LogOutUseCase
) {

    var cryptoCurrencies by mutableStateOf<List<CryptoCurrencyUiModel>>(value = emptyList())
        private set
    var action by mutableStateOf<Event<Action>?>(value = null)
        private set
    var watchListSummary by mutableStateOf<WatchListSummaryUiModel?>(value = null)
        private set
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
                            cryptoCurrencies = result.data.map { it.toUiModel() }
                            watchListSummary = result.data.toWatchListSummaryUiModel()
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

    fun onItemClicked(id: String) {
        action = Event(Action.OnItemClicked(id = id))
    }

    fun onSignOutClicked() {
        screenState = ScreenState.Loading
        stateScope.launch {
            logOut().onEach { result ->
                when (result) {
                    is AuthResult.Failure ->  screenState = ScreenState.Error(message = result.error)
                    is AuthResult.Success -> action = Event(Action.OnSignOutClicked)
                }
            }.stateIn(scope = this)
        }
    }

    sealed class ScreenState {
        object Loading: ScreenState()
        object Normal: ScreenState()
        data class Error(val message: String): ScreenState()
    }

    sealed class Action {
        data class OnItemClicked(val id: String) : Action()
        object OnSignOutClicked : Action()
    }

    companion object {

        private const val CURRENCIES_KEY = "currencies"
        private const val WATCHLIST_KEY = "watchlist"

        fun getSaver(
            stateScope: CoroutineScope,
            getCryptoCurrenciesForWatchList: GetCryptoCurrenciesForWatchListUseCase,
            getCryptoCurrenciesInWatchList: GetCryptoCurrenciesInWatchListUseCase,
            logOut: LogOutUseCase
        ): Saver<WatchListScreenState, *> = mapSaver(
            save = {
                mapOf(CURRENCIES_KEY to it.cryptoCurrencies)
            },
            restore = {
                WatchListScreenState(
                    stateScope = stateScope,
                    getCryptoCurrenciesInWatchList = getCryptoCurrenciesInWatchList,
                    getCryptoCurrenciesForWatchList = getCryptoCurrenciesForWatchList,
                    logOut = logOut
                ).apply {
                    cryptoCurrencies = it[CURRENCIES_KEY] as List<CryptoCurrencyUiModel>
                    watchListSummary = it[WATCHLIST_KEY] as WatchListSummaryUiModel?
                }
            }
        )
    }
}