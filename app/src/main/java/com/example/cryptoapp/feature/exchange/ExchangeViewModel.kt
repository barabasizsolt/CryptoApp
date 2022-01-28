package com.example.cryptoapp.feature.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.model.RefreshType
import com.example.cryptoapp.data.model.Result
import com.example.cryptoapp.data.model.exchange.Exchange
import com.example.cryptoapp.domain.exchange.GetExchangesUseCase
import com.example.cryptoapp.feature.shared.utils.eventFlow
import com.example.cryptoapp.feature.shared.utils.pushEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ExchangeViewModel(private val useCase: GetExchangesUseCase) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    private val exchanges = MutableStateFlow<List<Exchange>?>(null)
    private val shouldShowError = MutableStateFlow(false)
    val listItems = combine(exchanges, shouldShowError) { exchanges, shouldShowError ->
        if (shouldShowError) {
            exchanges?.map { it.toListItem() } ?: listOf(ExchangeListItem.ErrorState())
        } else {
            when {
                exchanges == null || exchanges.isEmpty() -> emptyList()
                else -> exchanges.map { it.toListItem() } + ExchangeListItem.LoadMore()
            }
        }
    }

    private val _event = eventFlow<Event>()
    val event: SharedFlow<Event> = _event

    init {
        refreshData(isForceRefresh = false)
    }

    fun refreshData(isForceRefresh: Boolean) {
        if (!_isRefreshing.value) {
            viewModelScope.launch {
                _isRefreshing.value = true
                shouldShowError.value = false
                when (
                    val result = useCase(
                        refreshType = when {
                            isForceRefresh -> RefreshType.FORCE_REFRESH
                            exchanges.value.isNullOrEmpty() -> RefreshType.CACHE_IF_POSSIBLE
                            else -> RefreshType.NEXT_PAGE
                        }
                    )
                ) {
                    is Result.Success -> {
                        exchanges.value = result.data
                    }
                    is Result.Failure -> {
                        shouldShowError.value = true
                        if (exchanges.value != null) {
                            _event.pushEvent(Event.ShowErrorMessage(errorMessage = "Failed to load exchanges"))
                        }
                    }
                }
                _isRefreshing.value = false
            }
        }
    }

    fun onExchangeItemClicked(id: String) = _event.pushEvent(Event.LogExchangeId(id))

    private fun Exchange.toListItem() = ExchangeListItem.Exchange(
        exchangeId = id,
        name = name,
        logo = image,
        trustScore = trustScore.toString(),
        volume = volume.toString()
    )

    sealed class Event {
        data class LogExchangeId(val id: String) : Event()

        data class ShowErrorMessage(val errorMessage: String) : Event()
    }
}
