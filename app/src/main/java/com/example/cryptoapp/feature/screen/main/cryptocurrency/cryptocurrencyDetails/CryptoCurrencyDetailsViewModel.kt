package com.example.cryptoapp.feature.screen.main.cryptocurrency.cryptocurrencyDetails

import android.text.Html
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.R
import com.example.cryptoapp.data.model.Result
import com.example.cryptoapp.data.model.cryptocurrency.CryptoCurrencyDetails
import com.example.cryptoapp.domain.useCase.cryptocurrency.GetCryptoCurrencyDetailsUseCase
import com.example.cryptoapp.domain.useCase.cryptocurrency.GetCryptoCurrencyHistoryUseCase
import com.example.cryptoapp.feature.screen.main.cryptocurrency.cryptocurrencyDetails.helpers.UnitOfTimeType
import com.example.cryptoapp.data.shared.ChartHistory
import com.example.cryptoapp.feature.shared.utils.ChipItem
import com.example.cryptoapp.feature.shared.utils.convertToCompactPrice
import com.example.cryptoapp.feature.shared.utils.convertToPrice
import com.example.cryptoapp.feature.shared.utils.eventFlow
import com.example.cryptoapp.feature.shared.utils.formatInput
import com.example.cryptoapp.feature.shared.utils.getFormattedHour
import com.example.cryptoapp.feature.shared.utils.getFormattedTime
import com.example.cryptoapp.feature.shared.utils.ordinalOf
import com.example.cryptoapp.feature.shared.utils.pushEvent
import com.example.cryptoapp.feature.shared.utils.toChartDataSet
import com.example.cryptoapp.auth.useCase.GetCurrentUserUseCase
import com.example.cryptoapp.firestore.useCase.AddCryptoCurrencyToWatchListUseCase
import com.example.cryptoapp.firestore.useCase.DeleteCryptoCurrencyFromWatchList
import com.example.cryptoapp.firestore.useCase.IsCryptoCurrencyAddedToWatchList
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class CryptoCurrencyDetailsViewModel(
    private val uuid: String,
    private val detailsUseCase: GetCryptoCurrencyDetailsUseCase,
    private val historyUseCase: GetCryptoCurrencyHistoryUseCase,
    private val addCryptoCurrencyToWatchListUseCase: AddCryptoCurrencyToWatchListUseCase,
    private val deleteCryptoCurrencyFromWatchList: DeleteCryptoCurrencyFromWatchList,
    private val isCryptoCurrencyAddedToWatchList: IsCryptoCurrencyAddedToWatchList,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _isAddedToWatchList = MutableStateFlow(false)
    val isAddedToWatchList: StateFlow<Boolean> = _isAddedToWatchList

    private val shouldShowError = MutableStateFlow(false)

    private val details = MutableStateFlow<CryptoCurrencyDetails?>(null)
    private val history = MutableStateFlow<List<ChartHistory>?>(null)

    private val cryptoTimePeriods = listOf("24h", "7d", "1y", "5y")
    private var timePeriod: String = cryptoTimePeriods[0]
    private var unitOfTimeType: UnitOfTimeType = UnitOfTimeType.UNIT_24H

    private var isDetailsErrorEmitted: Boolean = false
    private var isHistoryErrorEmitted: Boolean = false

    private val chips = MutableStateFlow(
        listOf(
            ChipItem.CryptoCurrencyDetailsChipItem(
                chipItemId = UnitOfTimeType.UNIT_24H.ordinal,
                chipTextId = R.string.chip_24hr,
                isChecked = true
            ),
            ChipItem.CryptoCurrencyDetailsChipItem(
                chipItemId = UnitOfTimeType.UNIT_7D.ordinal,
                chipTextId = R.string.chip_7d,
                isChecked = false
            ),
            ChipItem.CryptoCurrencyDetailsChipItem(
                chipItemId = UnitOfTimeType.UNIT_1Y.ordinal,
                chipTextId = R.string.chip_1y,
                isChecked = false
            ),
            ChipItem.CryptoCurrencyDetailsChipItem(
                chipItemId = UnitOfTimeType.UNIT_MAX.ordinal,
                chipTextId = R.string.chip_max,
                isChecked = false
            )
        )
    )

    val listItem = combine(details, history, shouldShowError) {
        details, history, shouldShowError ->
        when {
            (shouldShowError && details != null && history != null) || (details != null && history != null) ->
                listOf(
                    details.toCryptoCurrencyLogoListItem(),
                    CryptoCurrencyDetailsListItem.CryptoCurrencyChart(
                        data = history.toChartDataSet(timePeriod = timePeriod),
                        unitOfTimeType = unitOfTimeType
                    ),
                    CryptoCurrencyDetailsListItem.CryptoCurrencyChipGroup(chips = chips.value),
                    details.toCryptoCurrencyHeaderListItem(),
                    details.toCryptoCurrencyBodyListItem()
                )
            shouldShowError && history == null && details != null ->
                listOf(
                    details.toCryptoCurrencyLogoListItem(),
                    CryptoCurrencyDetailsListItem.ErrorState(),
                    CryptoCurrencyDetailsListItem.CryptoCurrencyChipGroup(chips = emptyList()),
                    details.toCryptoCurrencyHeaderListItem(),
                    details.toCryptoCurrencyBodyListItem()
                )
            shouldShowError && (details == null || history == null) -> listOf(CryptoCurrencyDetailsListItem.ErrorState())
            else -> emptyList()
        }
    }

    private val _event = eventFlow<Event>()
    val event: SharedFlow<Event> = _event

    init {
        refreshData()
        isCryptoCurrencyAddedToWatchList(id = uuid).onEach {
            _isAddedToWatchList.value = it
        }.launchIn(scope = viewModelScope)
    }

    fun refreshData() {
        if (!_isRefreshing.value) {
            _isRefreshing.value = true
            viewModelScope.launch {
                listOf(
                    async {
                        when (
                            val result = detailsUseCase(uuid = uuid,)
                        ) {
                            is Result.Success -> {
                                shouldShowError.value = false
                                details.value = result.data
                            }
                            is Result.Failure -> {
                                shouldShowError.value = true
                                if (details.value != null && !isHistoryErrorEmitted) {
                                    isDetailsErrorEmitted = true
                                    _event.pushEvent(Event.ShowErrorMessage(errorMessage = "Failed to load cryptocurrency details"))
                                }
                            }
                        }
                    },
                    async {
                        when (
                            val result = historyUseCase(
                                uuid = uuid,
                                timePeriod = timePeriod
                            )
                        ) {
                            is Result.Success -> {
                                shouldShowError.value = false
                                history.value = result.data
                            }
                            is Result.Failure -> {
                                shouldShowError.value = true
                                if (history.value != null && !isDetailsErrorEmitted) {
                                    isHistoryErrorEmitted = true
                                    _event.pushEvent(Event.ShowErrorMessage(errorMessage = "Failed to load cryptocurrency history"))
                                }
                            }
                        }
                    }
                ).awaitAll()
                _isRefreshing.value = false
                isDetailsErrorEmitted = false
                isHistoryErrorEmitted = false
            }
        }
    }

    private fun refreshCoinHistory() {
        if (!_isRefreshing.value) {
            _isRefreshing.value = true
            viewModelScope.launch {
                when (
                    val result = historyUseCase(
                        uuid = uuid,
                        timePeriod = timePeriod
                    )
                ) {
                    is Result.Success -> {
                        shouldShowError.value = false
                        history.value = result.data
                    }
                    is Result.Failure -> {
                        shouldShowError.value = true
                        history.value = null
                        _event.pushEvent(Event.ShowErrorMessage(errorMessage = "Failed to load cryptocurrency history"))
                    }
                }
                _isRefreshing.value = false
            }
        }
    }

    private fun CryptoCurrencyDetails.toCryptoCurrencyLogoListItem() = CryptoCurrencyDetailsListItem.CryptoCurrencyLogo(
        logo = iconUrl,
        name = name,
        symbol = symbol
    )

    private fun CryptoCurrencyDetails.toCryptoCurrencyHeaderListItem() = CryptoCurrencyDetailsListItem.CryptoCurrencyHeader(
        price = price.convertToPrice(),
        symbolWithValue = "$symbol/USD - AVG - ${System.currentTimeMillis().getFormattedHour()}",
        percentageChange = change,
        marketCap = marketCap.convertToCompactPrice(),
        volume = volume.convertToCompactPrice()
    )

    private fun CryptoCurrencyDetails.toCryptoCurrencyBodyListItem() = CryptoCurrencyDetailsListItem.CryptoCurrencyBody(
        rank = rank.ordinalOf(),
        supply = totalSupply.formatInput(),
        circulating = circulating.formatInput(),
        btcPrice = "${String.format("%.7f", btcPrice.toDouble())} Btc",
        allTimeHighDate = allTimeHigh.timestamp.getFormattedTime(),
        allTimeHighPrice = allTimeHigh.price.convertToPrice(),
        description = Html.fromHtml(description, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    )

    fun onChipClicked(timeType: UnitOfTimeType) = when (timeType) {
        UnitOfTimeType.UNIT_24H -> {
            timePeriod = cryptoTimePeriods[0]
            unitOfTimeType = UnitOfTimeType.UNIT_24H
        }
        UnitOfTimeType.UNIT_7D -> {
            timePeriod = cryptoTimePeriods[1]
            unitOfTimeType = UnitOfTimeType.UNIT_7D
        }
        UnitOfTimeType.UNIT_1Y -> {
            timePeriod = cryptoTimePeriods[2]
            unitOfTimeType = UnitOfTimeType.UNIT_1Y
        }
        UnitOfTimeType.UNIT_MAX -> {
            timePeriod = cryptoTimePeriods[3]
            unitOfTimeType = UnitOfTimeType.UNIT_MAX
        }
    }.let {
        onChipListChanged(timeType = timeType)
        refreshCoinHistory()
    }

    private fun onChipListChanged(timeType: UnitOfTimeType) {
        val currentChips = chips.value
        chips.value = currentChips.map {
            if (it.chipItemId == timeType.ordinal) {
                ChipItem.CryptoCurrencyDetailsChipItem(
                    chipItemId = it.chipItemId,
                    chipTextId = it.chipTextId,
                    isChecked = true
                )
            } else {
                ChipItem.CryptoCurrencyDetailsChipItem(
                    chipItemId = it.chipItemId,
                    chipTextId = it.chipTextId,
                    isChecked = false
                )
            }
        }
    }

    fun addOrRemoveFromWatchList() {
        if (isAddedToWatchList.value) {
            deleteCryptoCurrencyFromWatchList(id = uuid)
        } else {
            addCryptoCurrencyToWatchListUseCase(id = uuid)
        }
    }

    sealed class Event {
        data class ShowErrorMessage(val errorMessage: String) : Event()
    }
}
