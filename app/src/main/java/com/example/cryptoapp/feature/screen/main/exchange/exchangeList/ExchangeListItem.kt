package com.example.cryptoapp.feature.screen.main.exchange.exchangeList

import com.example.cryptoapp.feature.shared.utils.ListItem
import com.example.cryptoapp.feature.shared.utils.convertToCompactPrice
import com.example.cryptoapp.feature.shared.utils.formatInput

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
        val volume: String,
        val rank: Long,
        val yearEstablished: String
    ) : ExchangeListItem() {
        override val id = "exchanges_$exchangeId"
        val formattedVolume = "Btc ${volume.formatInput()}"
        val exchangeTrustScore = "$trustScore/10.0"
        val formattedRank = "#$rank"
    }
}
