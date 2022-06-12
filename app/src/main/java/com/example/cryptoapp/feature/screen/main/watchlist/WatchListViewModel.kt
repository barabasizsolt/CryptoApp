package com.example.cryptoapp.feature.screen.main.watchlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.model.RefreshType
import com.example.cryptoapp.data.model.Result
import com.example.cryptoapp.data.model.cryptocurrency.CryptoCurrency
import com.example.cryptoapp.domain.useCase.cryptocurrency.GetCryptoCurrenciesForWatchListUseCase
import com.example.cryptoapp.firestore.useCase.AddCryptoCurrencyToWatchListUseCase
import com.example.cryptoapp.firestore.useCase.DeleteCryptoCurrencyFromWatchList
import com.example.cryptoapp.firestore.useCase.GetCryptoCurrenciesInWatchListUseCase
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WatchListViewModel(
    private val getCryptoCurrenciesForWatchList: GetCryptoCurrenciesForWatchListUseCase,
    private val getCryptoCurrenciesInWatchList: GetCryptoCurrenciesInWatchListUseCase,
    private val addCryptoCurrencyToWatchList: AddCryptoCurrencyToWatchListUseCase,
    private val deleteCryptoCurrencyFromWatchList: DeleteCryptoCurrencyFromWatchList
) : ViewModel() {

    var cryptoCurrencies by mutableStateOf<List<CryptoCurrency>?>(value = null)
        private set
    var screenState by mutableStateOf<ScreenState>(value = ScreenState.Loading)

    init {
        refreshData()
    }

    fun refreshData() {
        screenState = ScreenState.Loading
        viewModelScope.launch {
            getCryptoCurrenciesInWatchList().onEach { uuids ->
                if (uuids.isNotEmpty()) {
                    when (
                        val result = getCryptoCurrenciesForWatchList(
                            uuids = uuids,
                            refreshType = RefreshType.FORCE_REFRESH
                        )
                    ) {
                        is Result.Success -> {
                            println("UUids: ${result.data.map { it.name }}")
                            cryptoCurrencies = result.data
                            screenState = ScreenState.Normal
                        }
                        is Result.Failure -> {
                            screenState = ScreenState.ShowFirstLoadingError
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

    sealed class ScreenState {

        object Loading: ScreenState()

        object Normal: ScreenState()

        data class ShowSnackBarError(val message: String) : ScreenState()

        object ShowFirstLoadingError : ScreenState()
    }
}