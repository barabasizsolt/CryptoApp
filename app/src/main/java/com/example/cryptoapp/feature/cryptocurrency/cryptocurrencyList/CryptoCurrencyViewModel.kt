package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.model.RefreshType
import com.example.cryptoapp.data.model.Result
import com.example.cryptoapp.data.model.cryptoCurrency.CryptoCurrency
import com.example.cryptoapp.domain.cryptocurrency.Constant.sortingParams
import com.example.cryptoapp.domain.cryptocurrency.Constant.sortingTypes
import com.example.cryptoapp.domain.cryptocurrency.Constant.tags
import com.example.cryptoapp.domain.cryptocurrency.Constant.timePeriods
import com.example.cryptoapp.domain.cryptocurrency.GetCryptoCurrenciesUseCase
import com.example.cryptoapp.feature.shared.eventFlow
import com.example.cryptoapp.feature.shared.pushEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Locale

class CryptoCurrencyViewModel(private val useCase: GetCryptoCurrenciesUseCase) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    private val cryptoCurrencies = MutableStateFlow<List<CryptoCurrency>?>(null)
    private val shouldShowError = MutableStateFlow(false)

    private var selectedTags: List<String> = listOf()
    private var selectedTimePeriod: Int = 1
    private var selectedSortingCriteria: Int = 4

    val listItems = combine(cryptoCurrencies, shouldShowError) { cryptoCurrencies, shouldShowError ->
        if (shouldShowError) {
            listOf(CryptoCurrencyListItem.ErrorState())
        } else {
            if (cryptoCurrencies == null) {
                emptyList()
            } else {
                cryptoCurrencies.map {
                    it.toListItem(
                        timePeriods[selectedTimePeriod].uppercase(Locale.getDefault())
                    )
                } + CryptoCurrencyListItem.LoadMore()
            }
        }
    }

    private val _dialogEvent = eventFlow<Pair<FilterChip, Event.DialogEvent>>()
    val dialogEvent: SharedFlow<Pair<FilterChip, Event.DialogEvent>> = _dialogEvent
    private val _event = eventFlow<Event>()
    val event: SharedFlow<Event> = _event

    init {
        refreshData(isForceRefresh = false)
    }

    fun refreshData(
        orderBy: String = sortingParams[selectedSortingCriteria].first,
        orderDirection: String = sortingParams[selectedSortingCriteria].second,
        cryptoTags: List<String> = selectedTags,
        timePeriod: String = timePeriods[selectedTimePeriod],
        isForceRefresh: Boolean
    ) {
        if (!isRefreshing.value) {
            viewModelScope.launch {
                _isRefreshing.value = true
                shouldShowError.value = false
                when (
                    val result = useCase(
                        orderBy = orderBy,
                        orderDirection = orderDirection,
                        tags = cryptoTags,
                        timePeriod = timePeriod,
                        refreshType = when {
                            isForceRefresh -> RefreshType.FORCE_REFRESH
                            cryptoCurrencies.value.isNullOrEmpty() -> RefreshType.CACHE_IF_POSSIBLE
                            else -> RefreshType.NEXT_PAGE
                        }
                    )
                ) {
                    is Result.Success -> {
                        cryptoCurrencies.value = result.data
                    }
                    is Result.Failure -> {
                        shouldShowError.value = true
                    }
                }
                _isRefreshing.value = false
            }
        }
    }

    fun onChipClicked(filterChip: FilterChip) = when (filterChip) {
        FilterChip.TAG_CHIP -> _dialogEvent.pushEvent(
            Pair(
                FilterChip.TAG_CHIP,
                Event.DialogEvent(
                    dialogElements = tags,
                    selectedItems = selectedTags,
                    dialogType = DialogType.MULTI_CHOICE
                )
            )
        )
        FilterChip.SORTING_CHIP -> _dialogEvent.pushEvent(
            Pair(
                FilterChip.SORTING_CHIP,
                Event.DialogEvent(
                    dialogElements = sortingTypes,
                    lastSelectedItemIndex = selectedSortingCriteria
                )
            )
        )
        FilterChip.TIME_PERIOD_CHIP -> _dialogEvent.pushEvent(
            Pair(
                FilterChip.TIME_PERIOD_CHIP,
                Event.DialogEvent(
                    dialogElements = timePeriods,
                    lastSelectedItemIndex = selectedTimePeriod
                )
            )
        )
    }

    fun onDialogItemSelected(filterChip: FilterChip, selectedItemIndex: Int = 0, selectedItems: List<String> = listOf()) = when (filterChip) {
        FilterChip.TAG_CHIP -> selectedTags = selectedItems
        FilterChip.SORTING_CHIP -> selectedSortingCriteria = selectedItemIndex
        FilterChip.TIME_PERIOD_CHIP -> selectedTimePeriod = selectedItemIndex
    }.also {
        refreshData(
            orderBy = sortingParams[selectedSortingCriteria].first,
            orderDirection = sortingParams[selectedSortingCriteria].second,
            cryptoTags = selectedTags,
            timePeriod = timePeriods[selectedTimePeriod],
            isForceRefresh = true
        )
    }

    fun onCryptoCurrencyItemClicked(id: String) = _event.pushEvent(Event.OpenDetailsPageEvent(id))

    private fun CryptoCurrency.toListItem(timePeriod: String) = CryptoCurrencyListItem.Crypto(
        cryptoCurrency = this,
        timePeriod = timePeriod.uppercase(Locale.getDefault())
    )

    sealed class Event {
        data class DialogEvent(
            val dialogElements: List<String>,
            val lastSelectedItemIndex: Int = 0,
            val selectedItems: List<String> = listOf(),
            val dialogType: DialogType = DialogType.SINGLE_CHOICE
        ) : Event()

        data class OpenDetailsPageEvent(val id: String) : Event()
    }
}
