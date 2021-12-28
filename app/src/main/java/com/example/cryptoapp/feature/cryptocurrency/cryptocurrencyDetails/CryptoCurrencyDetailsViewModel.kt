package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails

import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.example.cryptoapp.feature.news.NewsViewModel
import com.example.cryptoapp.feature.shared.convertToCompactPrice
import com.example.cryptoapp.feature.shared.convertToPrice
import com.example.cryptoapp.feature.shared.eventFlow
import com.example.cryptoapp.feature.shared.formatInput
import com.example.cryptoapp.feature.shared.getFormattedHour
import com.example.cryptoapp.feature.shared.getFormattedTime
import com.example.cryptoapp.feature.shared.getTime
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Month

class CryptoCurrencyDetailsViewModel(
    private val uuid: String,
    private val chartBackgroundColor: Int,
    private val chartTextColor: Int,
    private val chartColor: Int,
    private val detailsUseCase: GetCryptoCurrencyDetailsUseCase,
    private val historyUseCase: GetCryptoCurrencyHistoryUseCase
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    private val shouldShowError = MutableStateFlow(false)
    private val details = MutableStateFlow<CryptoCurrencyDetails?>(null)
    private val history = MutableStateFlow<List<CryptoHistoryItem>?>(null)
    private var timePeriod: String = HOUR24
    private var isDescriptionExpanded: Boolean = false
    private var isChartInitialized: Boolean = false

    val listItem = combine(details, history, shouldShowError) {
        details, history, shouldShowError ->
        if (shouldShowError) {
            emptyList()
        } else {
            if (details != null && history != null) {
                listOf(
                    details.toCryptoCurrencyLogoListItem(),
                    CryptoCurrencyDetailsListItem.CryptoCurrencyChart(
                        data = getDataSet(),
                        chartBackgroundColor = chartBackgroundColor,
                        chartTextColor = chartTextColor,
                        chartColor = chartColor,
                        isChartInitialized = isChartInitialized
                    ),
                    CryptoCurrencyDetailsListItem.CryptoCurrencyChipGroup(),
                    details.toCryptoCurrencyHeaderListItem(),
                    details.toCryptoCurrencyBodyListItem()
                )
            } else {
                emptyList()
            }
        }
    }

    private val _event = eventFlow<NewsViewModel.Event>()
    val event: SharedFlow<NewsViewModel.Event> = _event

    init {
        refreshCoinDetails(uuid = uuid)
        refreshCoinHistory(uuid = uuid, timePeriod = timePeriod)
    }

    private fun refreshCoinDetails(uuid: String) {
        if (!_isRefreshing.value) {
            viewModelScope.launch {
                _isRefreshing.value = true
                shouldShowError.value = false
                when (val result = detailsUseCase(uuid = uuid)) {
                    is Result.Success -> {
                        details.value = result.data
                    }
                    is Result.Failure -> {
                        shouldShowError.value = true
                    }
                }
                _isRefreshing.value = false
            }
        }
    }

    private fun refreshCoinHistory(uuid: String, timePeriod: String) {
        viewModelScope.launch {
            when (val result = historyUseCase(uuid = uuid, timePeriod = timePeriod)) {
                is Result.Success -> {
                    history.value = result.data
                }
                is Result.Failure -> {
                    shouldShowError.value = true
                }
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
        rank = rank.toString(),
        supply = totalSupply.formatInput(),
        circulating = circulating.formatInput(),
        btcPrice = String.format("%.7f", btcPrice.toDouble()) + " Btc",
        allTimeHighDate = allTimeHigh.timestamp.getFormattedTime(),
        allTimeHighPrice = allTimeHigh.price.convertToPrice(),
        description = Html.fromHtml(description, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    )

    private fun List<CryptoHistoryItem>.toChartArray(timePeriod: String): ArrayList<Entry> {
        val currencyHistory: ArrayList<Entry> = ArrayList()
        // TODO:refactor it
        when (timePeriod) {
            HOUR24 -> {
                val groupedHistory = sortedMapOf<Int, MutableList<Double>>()

                this.forEach { curr ->
                    val time = curr.timestamp.getTime().hour
                    if (!groupedHistory.containsKey(time)) {
                        groupedHistory[time] = mutableListOf()
                    }
                    groupedHistory[time]?.add(curr.price.toDouble())
                }

                groupedHistory.forEach { elem ->
                    currencyHistory.add(Entry(elem.key.toFloat(), elem.value.maxOf { it }.toFloat()))
                }
            }
            DAY7 -> {
                val groupedHistory = mutableMapOf<DayOfWeek, MutableList<Double>>()

                this.forEach { curr ->
                    val dayOfWeek = curr.timestamp.getTime().dayOfWeek
                    if (!groupedHistory.containsKey(dayOfWeek)) {
                        groupedHistory[dayOfWeek] = mutableListOf()
                    }
                    groupedHistory[dayOfWeek]?.add(curr.price.toDouble())
                }

                groupedHistory.toSortedMap(compareBy { it.ordinal }).forEach { elem ->
                    currencyHistory.add(Entry(elem.key.value.toFloat(), elem.value.maxOf { it }.toFloat()))
                }
            }
            YEAR1 -> {
                val groupedHistory = mutableMapOf<Month, MutableList<Double>>()

                this.forEach { curr ->
                    val month = curr.timestamp.getTime().month
                    if (!groupedHistory.containsKey(month)) {
                        groupedHistory[month] = mutableListOf()
                    }
                    groupedHistory[month]?.add(curr.price.toDouble())
                }

                groupedHistory.toSortedMap(compareBy { it.ordinal }).forEach { elem ->
                    currencyHistory.add(Entry(elem.key.value.toFloat(), elem.value.maxOf { it }.toFloat()))
                }
            }
            YEAR6 -> {
                val groupedHistory = mutableMapOf<Int, MutableList<Double>>()

                this.forEach { curr ->
                    val year = curr.timestamp.getTime().year
                    if (!groupedHistory.containsKey(year)) {
                        groupedHistory[year] = mutableListOf()
                    }
                    groupedHistory[year]?.add(curr.price.toDouble())
                }
                groupedHistory.toSortedMap(compareBy { it }).forEach {
                    elem ->
                    currencyHistory.add(Entry(elem.key.toFloat(), elem.value.maxOf { it }.toFloat()))
                }
            }
        }

        return currencyHistory
    }

    private fun getDataSet() = LineDataSet(history.value?.toChartArray(timePeriod = timePeriod), "data")
        .also { lineDataSet ->
            lineDataSet.lineWidth = 3f
            lineDataSet.color = chartTextColor
            lineDataSet.highLightColor = chartColor
            lineDataSet.setDrawValues(false)
            lineDataSet.circleRadius = 10f
            lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
            lineDataSet.cubicIntensity = 0.1f
            lineDataSet.setDrawFilled(true)
            lineDataSet.fillColor = chartColor
            lineDataSet.fillAlpha = 255
            lineDataSet.setDrawCircles(false)
        }

    fun onChipClicked(chipType: ChipType) = when (chipType) {
        ChipType.CHIP_24H -> timePeriod = HOUR24
        ChipType.CHIP_7D -> timePeriod = DAY7
        ChipType.CHIP_1Y -> timePeriod = YEAR1
        ChipType.CHIP_6Y -> timePeriod = YEAR6
    }.let {
        isChartInitialized = true
        refreshCoinHistory(uuid = uuid, timePeriod = timePeriod)
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
}
