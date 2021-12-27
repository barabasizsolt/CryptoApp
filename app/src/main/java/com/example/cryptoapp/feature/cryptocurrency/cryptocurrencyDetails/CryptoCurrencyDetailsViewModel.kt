package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails

import android.text.Html
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.model.Result
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.details.CryptoCurrencyDetails
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.details.CryptoCurrencyDetailsUIModel
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.history.CryptoCurrencyHistory
import com.example.cryptoapp.domain.cryptocurrency.GetCryptoCurrencyDetailsUseCase
import com.example.cryptoapp.domain.cryptocurrency.GetCryptoCurrencyHistoryUseCase
import com.example.cryptoapp.feature.shared.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CryptoCurrencyDetailsViewModel(
    uuid: String,
    private val detailsUseCase: GetCryptoCurrencyDetailsUseCase,
    private val historyUseCase: GetCryptoCurrencyHistoryUseCase
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    private val shouldShowError = MutableStateFlow(false)
    private val details = MutableStateFlow<CryptoCurrencyDetails?>(null)
    private val history = MutableStateFlow<CryptoCurrencyHistory?>(null)

    val listItem = combine(details, history, shouldShowError) {
        details, history, shouldShowError ->
        if(shouldShowError){

        }
        else{
            if(details != null && history != null){
                listOf(
                    details.toCryptoCurrencyLogoListItem(),
                    CryptoCurrencyDetailsListItem.CryptoCurrencyChipGroup(),
                    details.toCryptoCurrencyHeaderListItem(),
                    details.toCryptoCurrencyBodyListItem()
                ) + history.toCryptoCurrencyChartListItem()
            }
        }
    }

    init {
        refreshCoinDetails(uuid = uuid)
    }

    private fun refreshCoinDetails(uuid: String) {
        if(!_isRefreshing.value) {
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

    fun refreshCoinHistory(uuid: String, timePeriod: String) {
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

    private fun CryptoCurrencyHistory.toCryptoCurrencyChartListItem() = CryptoCurrencyDetailsListItem.CryptoCurrencyChart(
        history = history
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

    private fun CryptoCurrencyDetails.toCryptoCurrencyDetailsUIModel() = CryptoCurrencyDetailsUIModel(
        uuid = uuid,
        symbol = symbol,
        name = name,
        iconUrl = iconUrl,
        marketCap = marketCap,
        price = price,
        change = change,
        volume = volume
    )

    private fun CryptoCurrencyDetails.toCryptoCurrencyDetailsInfoUIModel() = CryptoCurrencyDetailsInfoUIModel(
        rank = rank.toString(),
        totalSupply = totalSupply,
        circulating = circulating,
        btcPrice = btcPrice,
        allTimeHigh = allTimeHigh,
        description = description
    )

    //val currentTime = System.currentTimeMillis().getTime()
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
