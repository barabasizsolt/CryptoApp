package com.example.cryptoapp.feature.main.cryptocurrency.cryptocurrencyList

import com.example.cryptoapp.data.model.cryptocurrency.CryptoCurrency
import com.example.cryptoapp.feature.shared.utils.ListItem
import com.example.cryptoapp.feature.shared.utils.convertToCompactPrice
import com.example.cryptoapp.feature.shared.utils.convertToPrice

sealed class CryptoCurrencyListItem : ListItem {

    data class ErrorState(
        val nothing: Any? = null
    ) : CryptoCurrencyListItem() {
        override val id = "errorState"
    }

    data class LoadMore(
        val nothing: Any? = null
    ) : CryptoCurrencyListItem() {
        override val id = "loadMore"
    }

    data class Crypto(
        val cryptoCurrency: CryptoCurrency,
        val timePeriod: String
    ) : CryptoCurrencyListItem() {
        override val id = "crypto_${cryptoCurrency.uuid}"
        val cryptoCurrencyId = cryptoCurrency.uuid
        val formattedPrice = cryptoCurrency.price.convertToPrice()
        val formattedVolume = cryptoCurrency.volume.convertToCompactPrice()
        val formattedMarketCap = cryptoCurrency.marketCap.convertToCompactPrice()
    }
}
