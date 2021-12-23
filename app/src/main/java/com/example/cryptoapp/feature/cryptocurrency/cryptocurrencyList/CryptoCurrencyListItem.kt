package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyList

import com.example.cryptoapp.data.model.cryptoCurrency.CryptoCurrency
import com.example.cryptoapp.feature.shared.ListItem
import com.example.cryptoapp.feature.shared.convertToCompactPrice
import com.example.cryptoapp.feature.shared.convertToPrice

sealed class CryptoCurrencyListItem : ListItem {

    data class LoadMore(
        val nothing: Any? = null
    ) : CryptoCurrencyListItem() {
        override val id = "loadMore"
    }

    data class Crypto(
        val cryptoCurrency: CryptoCurrency,
        val timePeriod: String
    ) : CryptoCurrencyListItem() {
        override val id = cryptoCurrency.uuid
        val formattedPrice = cryptoCurrency.price.convertToPrice()
        val formattedVolume = cryptoCurrency.volume.convertToCompactPrice()
        val formattedMarketCap = cryptoCurrency.marketCap.convertToCompactPrice()
    }
}
