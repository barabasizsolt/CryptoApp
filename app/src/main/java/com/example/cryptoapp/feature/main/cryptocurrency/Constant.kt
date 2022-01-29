package com.example.cryptoapp.feature.main.cryptocurrency
import java.util.Calendar

object Constant {
    const val ROTATE_180 = 180f
    const val ROTATE_360 = 360f

    private const val PRICE_FIELD = "price"
    private const val VOLUME_FIELD = "24hVolume"
    private const val MARKET_CAP_FIELD = "marketCap"
    private const val ASC = "asc"
    private const val DESC = "desc"

    val CALENDAR: Calendar = Calendar.getInstance()
    const val MAX_HOUR: Int = 24
    const val MAX_DAY: Int = 7
    const val MAX_MONTH: Int = 12

    const val CURRENCY_FIRE_STORE_PATH = "currencies"
    const val COIN_ID: String = "coin_id"

    const val HOUR24 = "24h"
    const val DAY7 = "7d"
    const val YEAR1 = "1y"
    const val YEAR6 = "5y"

    val tags = listOf(
        "defi",
        "stablecoin",
        "nft",
        "dex",
        "exchange",
        "staking",
        "dao",
        "meme",
        "privacy"
    )

    val timePeriods = listOf("3h", "24h", "7d", "30d", "3m", "1y", "3y", "5y")

    val sortingTypes = listOf(
        "Highest Price",
        "Lowest Price",
        "Highest 24H Volume",
        "Lowest 24H Volume",
        "Highest Market Cap",
        "Lowest Market Cap"
    )

    val sortingParams = arrayOf(
        Pair(PRICE_FIELD, DESC),
        Pair(PRICE_FIELD, ASC),
        Pair(VOLUME_FIELD, DESC),
        Pair(VOLUME_FIELD, ASC),
        Pair(MARKET_CAP_FIELD, DESC),
        Pair(MARKET_CAP_FIELD, ASC),
    )
}
