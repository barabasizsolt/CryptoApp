package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails

import com.example.cryptoapp.feature.shared.ListItem
import com.github.mikephil.charting.data.LineDataSet

sealed class CryptoCurrencyDetailsListItem : ListItem {

    data class ErrorState(
        val nothing: Any? = null
    ) : CryptoCurrencyDetailsListItem() {
        override val id = "errorState"
    }

    data class CryptoCurrencyLogo(
        val logo: String,
        val name: String,
        val symbol: String
    ) : CryptoCurrencyDetailsListItem() {
        override val id = "crypto_details_$name"
    }

    data class CryptoCurrencyChart(
        val data: LineDataSet,
        val chartBackgroundColor: Int,
        val chartTextColor: Int,
        val chartColor: Int,
        val isChartInitialized: Boolean
    ) : CryptoCurrencyDetailsListItem() {
        override val id = "crypto_details_chart"
    }

    data class CryptoCurrencyChipGroup(
        val nothing: Any? = null
    ) : CryptoCurrencyDetailsListItem() {
        override val id = "crypto_details_chip_group"
    }

    data class CryptoCurrencyHeader(
        val price: String,
        val symbolWithValue: String,
        val percentageChange: String,
        val marketCap: String,
        val volume: String,
    ) : CryptoCurrencyDetailsListItem() {
        override val id = "crypto_details_$price"
    }

    data class CryptoCurrencyBody(
        val rank: String,
        val supply: String,
        val circulating: String,
        val btcPrice: String,
        val allTimeHighDate: String,
        val allTimeHighPrice: String,
        val description: String
    ) : CryptoCurrencyDetailsListItem() {
        override val id = "crypto_details_$rank"
    }
}
