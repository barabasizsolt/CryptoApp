package com.barabasizsolt.feature.screen.main.cryptocurrency.cryptocurrencyList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barabasizsolt.feature.screen.main.cryptocurrency.Constant.sortingParams
import com.barabasizsolt.feature.screen.main.cryptocurrency.Constant.sortingTypes
import com.barabasizsolt.feature.screen.main.cryptocurrency.Constant.tags
import com.barabasizsolt.feature.screen.main.cryptocurrency.Constant.timePeriods
import com.barabasizsolt.feature.screen.main.cryptocurrency.cryptocurrencyList.helpers.DialogType
import com.barabasizsolt.feature.screen.main.cryptocurrency.cryptocurrencyList.helpers.FilterChip
import com.barabasizsolt.feature.shared.utils.eventFlow
import com.barabasizsolt.feature.shared.utils.pushEvent
import com.example.cryptoapp.data.model.RefreshType
import com.example.cryptoapp.data.model.Result
import com.example.cryptoapp.data.model.cryptocurrency.CryptoCurrency
import com.example.cryptoapp.domain.useCase.cryptocurrency.GetCryptoCurrenciesUseCase
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
            cryptoCurrencies?.map { it.toListItem(timePeriods[selectedTimePeriod].uppercase(Locale.getDefault())) }
                ?: listOf(CryptoCurrencyListItem.ErrorState())
        } else {
            when {
                cryptoCurrencies.isNullOrEmpty() -> emptyList()
                else -> cryptoCurrencies.map { it.toListItem(timePeriods[selectedTimePeriod].uppercase(Locale.getDefault())) } + CryptoCurrencyListItem.LoadMore()
            }
        }
    }

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
                        if (cryptoCurrencies.value != null) {
                            _event.pushEvent(Event.ShowErrorMessage(errorMessage = "Failed to load cryptocurrencies"))
                        }
                    }
                }
                _isRefreshing.value = false
            }
        }
    }

    fun onTimePeriodChipClicked() = _event.pushEvent(
        Event.ShowDialog(
            dialogElements = timePeriods,
            lastSelectedItemIndex = selectedTimePeriod,
            filterType = FilterChip.TIME_PERIOD_CHIP
        )
    )

    fun onSortingChipClicked() = _event.pushEvent(
        Event.ShowDialog(
            dialogElements = sortingTypes,
            lastSelectedItemIndex = selectedSortingCriteria,
            filterType = FilterChip.SORTING_CHIP
        )
    )

    fun onTagChipClicked() = _event.pushEvent(
        Event.ShowDialog(
            dialogElements = tags,
            selectedItems = selectedTags,
            dialogType = DialogType.MULTI_CHOICE,
            filterType = FilterChip.TAG_CHIP
        )
    )

    fun onDialogItemSelected(filterChip: FilterChip, selectedItemIndex: Int = 0, selectedItems: List<String> = listOf()) = when (filterChip) {
        FilterChip.TAG_CHIP -> selectedTags = selectedItems
        FilterChip.SORTING_CHIP -> selectedSortingCriteria = selectedItemIndex
        FilterChip.TIME_PERIOD_CHIP -> selectedTimePeriod = selectedItemIndex
    }.also {
        cryptoCurrencies.value = null
        refreshData(
            orderBy = sortingParams[selectedSortingCriteria].first,
            orderDirection = sortingParams[selectedSortingCriteria].second,
            cryptoTags = selectedTags,
            timePeriod = timePeriods[selectedTimePeriod],
            isForceRefresh = true
        )
    }

    fun onCryptoCurrencyItemClicked(id: String) = _event.pushEvent(Event.OpenDetailsPage(id))

    private fun CryptoCurrency.toListItem(timePeriod: String) = CryptoCurrencyListItem.Crypto(
        cryptoCurrency = this,
        timePeriod = timePeriod.uppercase(Locale.getDefault())
    )

    sealed class Event {
        data class ShowDialog(
            val dialogElements: List<String>,
            val lastSelectedItemIndex: Int = 0,
            val selectedItems: List<String> = listOf(),
            val dialogType: DialogType = DialogType.SINGLE_CHOICE,
            val filterType: FilterChip
        ) : Event()

        data class OpenDetailsPage(val cryptoCurrencyId: String) : Event()

        data class ShowErrorMessage(val errorMessage: String) : Event()
    }
}
