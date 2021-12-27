package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails

import android.text.Html
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anychart.anychart.DataEntry
import com.anychart.anychart.ValueDataEntry
import com.example.cryptoapp.data.model.Result
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.details.CryptoCurrencyDetails
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.history.CryptoHistoryItem
import com.example.cryptoapp.domain.cryptocurrency.GetCryptoCurrencyDetailsUseCase
import com.example.cryptoapp.domain.cryptocurrency.GetCryptoCurrencyHistoryUseCase
import com.example.cryptoapp.feature.cryptocurrency.Constant.CALENDAR
import com.example.cryptoapp.feature.cryptocurrency.Constant.DAY7
import com.example.cryptoapp.feature.cryptocurrency.Constant.HOUR24
import com.example.cryptoapp.feature.cryptocurrency.Constant.MAX_HOUR
import com.example.cryptoapp.feature.cryptocurrency.Constant.MAX_MONTH
import com.example.cryptoapp.feature.cryptocurrency.Constant.ROTATE_180
import com.example.cryptoapp.feature.cryptocurrency.Constant.ROTATE_360
import com.example.cryptoapp.feature.cryptocurrency.Constant.YEAR1
import com.example.cryptoapp.feature.cryptocurrency.Constant.YEAR6
import com.example.cryptoapp.feature.news.NewsViewModel
import com.example.cryptoapp.feature.shared.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.util.Calendar.HOUR_OF_DAY
import java.util.Calendar.MONTH

class CryptoCurrencyDetailsViewModel(
    private val uuid: String,
    private val chartBackgroundColor: String,
    private val chartTextColor: String,
    private val chartColor: String,
    private val detailsUseCase: GetCryptoCurrencyDetailsUseCase,
    private val historyUseCase: GetCryptoCurrencyHistoryUseCase
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    private val shouldShowError = MutableStateFlow(false)
    private val details = MutableStateFlow<CryptoCurrencyDetails?>(null)
    private val history = MutableStateFlow<List<CryptoHistoryItem>?>(null)
    private var timePeriod: String = HOUR24
    private var isDescriptionVisible: Boolean = false

    val listItem = combine(details, history, shouldShowError) {
        details, history, shouldShowError ->
        if (shouldShowError) {
            emptyList()
        } else {
            if (details != null && history != null) {
                listOf(
                    details.toCryptoCurrencyLogoListItem(),
                    CryptoCurrencyDetailsListItem.CryptoCurrencyChart(
                        history = history.toAnyChartArray(timeFrame = timePeriod),
                        chartBackgroundColor = chartBackgroundColor,
                        chartTextColor = chartTextColor,
                        chartColor = chartColor
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
        refreshCoinHistory(uuid = uuid, timePeriod = HOUR24)
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
        symbolWithValue = "$symbol/USD",
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

    private fun List<CryptoHistoryItem>.toAnyChartArray(timeFrame: String): MutableList<DataEntry> {
        val currencyHistory: MutableList<DataEntry> = ArrayList()
        // TODO:refactor it
        when (timeFrame) {
            HOUR24 -> {
                val groupedHistory = sortedMapOf<Int, MutableList<Double>>()
                val tmpHistory: MutableList<Pair<Int, Double?>> = mutableListOf()

                val currentHour: Int = CALENDAR.get(HOUR_OF_DAY)

                this.forEach { curr ->
                    val time = curr.timestamp.getTime().hour
                    if (!groupedHistory.containsKey(time)) {
                        groupedHistory[time] = mutableListOf()
                    }
                    if (curr.price.isNotBlank()) {
                        groupedHistory[time]?.add(curr.price.toDouble())
                    }
                }

                groupedHistory.forEach { elem ->
                    tmpHistory.add(Pair(elem.key, elem.value.maxOfOrNull { it }))
                }

                for (i in (currentHour - 12)..(currentHour + 12)) {
                    val idx = i.mod(MAX_HOUR)
                    if (idx < tmpHistory.size) {
                        val elem = tmpHistory[idx]
                        currencyHistory.add(ValueDataEntry(elem.first.toString() + ":00", elem.second))
                    }
                }
            }
            DAY7 -> {
                val groupedHistory = mutableMapOf<DayOfWeek, MutableList<Double>>()
                val tmpHistory: MutableList<Pair<DayOfWeek, Double?>> = mutableListOf()

                val currentDay: Int = LocalDate.now().dayOfWeek.value - 1

                this.toMutableList().sortWith(compareBy { it.timestamp.getTime().dayOfWeek.ordinal })
                this.forEach { curr ->
                    val dayOfWeek = curr.timestamp.getTime().dayOfWeek
                    if (!groupedHistory.containsKey(dayOfWeek)) {
                        groupedHistory[dayOfWeek] = mutableListOf()
                    }
                    if (curr.price.isNotBlank()) {
                        groupedHistory[dayOfWeek]?.add(curr.price.toDouble())
                    }
                }

                groupedHistory.forEach { elem ->
                    tmpHistory.add(Pair(elem.key, elem.value.maxOfOrNull { it }))
                }

                for (i in (currentDay - 3)..(currentDay + 3)) {
                    val idx = i.mod(MAX_HOUR)
                    if (idx < tmpHistory.size) {
                        val elem = tmpHistory[idx]
                        currencyHistory.add(ValueDataEntry(elem.first.name.substring(0, 3), elem.second))
                    }
                }
            }
            YEAR1 -> {
                val groupedHistory = mutableMapOf<Month, MutableList<Double>>()
                val tmpHistory: MutableList<Pair<Month, Double?>> = mutableListOf()

                val currentMonth: Int = CALENDAR.get(MONTH)

                this.toMutableList().sortWith(compareBy { it.timestamp.getTime().month.ordinal })
                this.forEach { curr ->
                    val month = curr.timestamp.getTime().month
                    if (!groupedHistory.containsKey(month)) {
                        groupedHistory[month] = mutableListOf()
                    }
                    if (curr.price.isNotBlank()) {
                        groupedHistory[month]?.add(curr.price.toDouble())
                    }
                }

                groupedHistory.forEach { elem ->
                    tmpHistory.add(Pair(elem.key, elem.value.maxOfOrNull { it }))
                }

                for (i in (currentMonth - 6)..(currentMonth + 6)) {
                    val idx = i.mod(MAX_MONTH)
                    if (idx < tmpHistory.size) {
                        val elem = tmpHistory[idx]
                        currencyHistory.add(ValueDataEntry(elem.first.name.substring(0, 3), elem.second))
                    }
                }
            }
            YEAR6 -> {
                val groupedHistory = mutableMapOf<String, MutableList<Double>>()

                this.toMutableList().sortWith(compareBy { it.timestamp.getTime().year })
                this.forEach { curr ->
                    val year = curr.timestamp.getTime().year.toString()
                    if (!groupedHistory.containsKey(year)) {
                        groupedHistory[year] = mutableListOf()
                    }
                    if (curr.price.isNotBlank()) {
                        groupedHistory[year]?.add(curr.price.toDouble())
                    }
                }

                groupedHistory.forEach { elem -> currencyHistory.add(ValueDataEntry(elem.key, elem.value.maxOfOrNull { it })) }
            }
        }

        return currencyHistory
    }

    fun onChipClicked(chipType: ChipType) = when(chipType) {
        ChipType.CHIP_24H -> timePeriod = HOUR24
        ChipType.CHIP_7D -> timePeriod = DAY7
        ChipType.CHIP_1Y -> timePeriod = YEAR1
        ChipType.CHIP_6Y -> timePeriod = YEAR6
    }.also {
        refreshCoinHistory(uuid = uuid, timePeriod = timePeriod)
    }

    fun onDescriptionArrowClicked(arrow: ImageView, description: TextView){
        if (isDescriptionVisible) {
            arrow.animate().rotation(ROTATE_360).start()
            description.visibility = GONE
            isDescriptionVisible = false
        } else {
            arrow.animate().rotation(ROTATE_180).start()
            description.visibility = VISIBLE
            isDescriptionVisible = true
        }
    }

    sealed class Event {
        data class RefreshChart(val data: List<CryptoHistoryItem>) : Event()
    }

    // val currentTime = System.currentTimeMillis().getTime()
//    var currentHour = currentTime.hour.toString()
//    var currentMinute = currentTime.minute.toString()
//    if (currentHour.toInt() < 10) {
//        currentHour = "0$currentHour"
//    }
//    if (currentMinute.toInt() < 10) {
//        currentMinute = "0$currentMinute"
//    }
//    val coinValueSymbol = coin.symbol + "/" + "USD" + " - AVG - " + currentHour + ":" + currentMinute
}
