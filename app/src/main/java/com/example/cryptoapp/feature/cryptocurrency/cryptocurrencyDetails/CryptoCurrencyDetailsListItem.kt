package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails

import com.anychart.anychart.DataEntry
import com.example.cryptoapp.feature.shared.ListItem

sealed class CryptoCurrencyDetailsListItem : ListItem {

    data class CryptoCurrencyLogo(
        val logo: String,
        val name: String,
        val symbol: String
    ) : CryptoCurrencyDetailsListItem() {
        override val id = "crypto_details_$name"
    }

    data class CryptoCurrencyChart(
        val history: MutableList<DataEntry> = mutableListOf(),
        val chartBackgroundColor: String,
        val chartTextColor: String,
        val chartColor: String
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
        override val id = "crypto_details_{$rank}"
    }
}
