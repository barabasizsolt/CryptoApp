package com.example.cryptoapp.feature.main.exchange

import com.example.cryptoapp.feature.shared.utils.ListItem
import com.example.cryptoapp.feature.shared.utils.convertToCompactPrice

sealed class ExchangeListItem : ListItem {

    data class ErrorState(
        val nothing: Any? = null
    ) : ExchangeListItem() {
        override val id = "errorState"
    }

    data class LoadMore(
        val nothing: Any? = null
    ) : ExchangeListItem() {
        override val id = "loadMore"
    }

    data class Exchange(
        val exchangeId: String,
        val name: String,
        val logo: String,
        val trustScore: String,
        val volume: String
    ) : ExchangeListItem() {
        override val id = "exchanges_$exchangeId"
        val formattedVolume = volume.convertToCompactPrice()
        val exchangeTrustScore = "$trustScore/10.0"
    }
}
