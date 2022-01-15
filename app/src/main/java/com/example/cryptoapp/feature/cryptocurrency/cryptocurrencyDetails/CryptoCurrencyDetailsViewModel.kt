package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails

import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.R
import com.example.cryptoapp.data.model.Result
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.details.CryptoCurrencyDetails
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.history.CryptoHistoryItem
import com.example.cryptoapp.domain.cryptocurrency.GetCryptoCurrencyDetailsUseCase
import com.example.cryptoapp.domain.cryptocurrency.GetCryptoCurrencyHistoryUseCase
import com.example.cryptoapp.feature.cryptocurrency.Constant.DAY7
import com.example.cryptoapp.feature.cryptocurrency.Constant.HOUR24
import com.example.cryptoapp.feature.cryptocurrency.Constant.ROTATE_180
import com.example.cryptoapp.feature.cryptocurrency.Constant.ROTATE_360
import com.example.cryptoapp.feature.cryptocurrency.Constant.YEAR1
import com.example.cryptoapp.feature.cryptocurrency.Constant.YEAR6
import com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails.helpers.UnitOfTimeType
import com.example.cryptoapp.feature.shared.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CryptoCurrencyDetailsViewModel(
    private val uuid: String,
    private val detailsUseCase: GetCryptoCurrencyDetailsUseCase,
    private val historyUseCase: GetCryptoCurrencyHistoryUseCase
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    private val shouldShowError = MutableStateFlow(false)
    private val details = MutableStateFlow<CryptoCurrencyDetails?>(null)
    private val history = MutableStateFlow<List<CryptoHistoryItem>?>(null)
    private var timePeriod: String = HOUR24
    private var unitOfTimeType: UnitOfTimeType = UnitOfTimeType.UNIT_24H
    private var isDescriptionExpanded: Boolean = false
    private var isDetailsErrorEmitted: Boolean = false
    private var isHistoryErrorEmitted: Boolean = false

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
                    CryptoCurrencyDetailsListItem.CryptoCurrencyChipGroup(
                        chips = listOf(
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
                                chipItemId = UnitOfTimeType.UNIT_6Y.ordinal,
                                chipTextId = R.string.chip_6y,
                                isChecked = false
                            )
                        )
                    ),
                    details.toCryptoCurrencyHeaderListItem(),
                    details.toCryptoCurrencyBodyListItem()
                )
            shouldShowError && history == null && details != null ->
                listOf(
                    details.toCryptoCurrencyLogoListItem(),
                    CryptoCurrencyDetailsListItem.ErrorState(),
                    CryptoCurrencyDetailsListItem.CryptoCurrencyChipGroup(emptyList()),
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
            timePeriod = HOUR24
            unitOfTimeType = UnitOfTimeType.UNIT_24H
        }
        UnitOfTimeType.UNIT_7D -> {
            timePeriod = DAY7
            unitOfTimeType = UnitOfTimeType.UNIT_7D
        }
        UnitOfTimeType.UNIT_1Y -> {
            timePeriod = YEAR1
            unitOfTimeType = UnitOfTimeType.UNIT_1Y
        }
        UnitOfTimeType.UNIT_6Y -> {
            timePeriod = YEAR6
            unitOfTimeType = UnitOfTimeType.UNIT_6Y
        }
    }.let {
        refreshCoinHistory()
    }

    fun onDescriptionArrowClicked(arrow: ImageView, description: TextView) = when (isDescriptionExpanded) {
        true -> {
            arrow.animate().rotation(ROTATE_360).start()
            description.maxLines = 3
            isDescriptionExpanded = false
        }
        false -> {
            arrow.animate().rotation(ROTATE_180).start()
            description.maxLines = 100
            isDescriptionExpanded = true
        }
    }

    sealed class Event {
        data class ShowErrorMessage(val errorMessage: String) : Event()
    }
}
