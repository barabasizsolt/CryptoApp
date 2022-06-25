package com.example.cryptoapp.wear.screen.main.watchlistDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.cryptoapp.data.model.Result
import com.example.cryptoapp.domain.useCase.cryptocurrency.GetCryptoCurrencyDetailsUseCase
import com.example.cryptoapp.firestore.useCase.DeleteCryptoCurrencyFromWatchListUseCase
import com.example.cryptoapp.wear.common.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@Composable
fun rememberWatchListDetailScreenState(
    stateScope: CoroutineScope = rememberCoroutineScope(),
    id: String,
    getCryptoCurrencyDetails: GetCryptoCurrencyDetailsUseCase = get(),
    deleteCryptoCurrencyFromWatchList: DeleteCryptoCurrencyFromWatchListUseCase = get()
): WatchListDetailScreenState = rememberSaveable(
    saver = WatchListDetailScreenState.getSaver(
        stateScope = stateScope,
        id = id,
        getCryptoCurrencyDetails = getCryptoCurrencyDetails,
        deleteCryptoCurrencyFromWatchList = deleteCryptoCurrencyFromWatchList
    )
) {
    WatchListDetailScreenState(
        stateScope = stateScope,
        id = id,
        getCryptoCurrencyDetails = getCryptoCurrencyDetails,
        deleteCryptoCurrencyFromWatchList = deleteCryptoCurrencyFromWatchList
    )
}

class WatchListDetailScreenState(
    private val stateScope: CoroutineScope,
    private val id: String,
    private val getCryptoCurrencyDetails: GetCryptoCurrencyDetailsUseCase,
    private val deleteCryptoCurrencyFromWatchList: DeleteCryptoCurrencyFromWatchListUseCase
) {
    var screenState by mutableStateOf<ScreenState>(value = ScreenState.Normal)
        private set
    var action by mutableStateOf<Event<Action>?>(value = null)
        private set
    var cryptoCurrencyDetail by mutableStateOf<CryptoCurrencyDetailUiModel?>(value = null)
        private set

    init {
        screenState = ScreenState.Loading
        stateScope.launch {
            when (val result =  getCryptoCurrencyDetails(uuid = id)) {
                is Result.Failure -> screenState = ScreenState.Error(message = result.exception.message.orEmpty())
                is Result.Success -> {
                    cryptoCurrencyDetail = result.data.toUiModel()
                    screenState = ScreenState.Normal
                }
            }
        }
    }

    fun onOpenAppClicked() {

    }

    fun onDeleteClicked() {
        deleteCryptoCurrencyFromWatchList(id = id)
        action = Event(Action.NavigateUp)
    }

    sealed class ScreenState {
        object Normal : ScreenState()
        object Loading : ScreenState()
        data class Error(val message: String) : ScreenState()
    }

    sealed class Action {
        object NavigateUp : Action()
    }

    companion object {

        private const val CRYPTO_DETAIL_KEY: String = "cryptoCurrencyDetail"

        fun getSaver(
            stateScope: CoroutineScope,
            id: String,
            getCryptoCurrencyDetails: GetCryptoCurrencyDetailsUseCase,
            deleteCryptoCurrencyFromWatchList: DeleteCryptoCurrencyFromWatchListUseCase
        ): Saver<WatchListDetailScreenState, *> = mapSaver(
            save = { mapOf(CRYPTO_DETAIL_KEY to it.cryptoCurrencyDetail) },
            restore = {
                WatchListDetailScreenState(
                    stateScope = stateScope,
                    id = id,
                    getCryptoCurrencyDetails = getCryptoCurrencyDetails,
                    deleteCryptoCurrencyFromWatchList = deleteCryptoCurrencyFromWatchList
                ).apply {
                    cryptoCurrencyDetail = it[CRYPTO_DETAIL_KEY] as CryptoCurrencyDetailUiModel?
                }
            }
        )
    }
}