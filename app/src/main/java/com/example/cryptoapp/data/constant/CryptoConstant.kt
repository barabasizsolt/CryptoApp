package com.example.cryptoapp.data.constant

import java.util.Calendar

object CryptoConstant {
    val CALENDAR: Calendar = Calendar.getInstance()
    const val MAX_HOUR: Int = 24
    const val MAX_DAY: Int = 7
    const val MAX_MONTH: Int = 12

    const val EMPTY_STR = "null"
    const val CURRENCY_FIRE_STORE_PATH = "currencies"
    const val COIN_ID: String = "coin_id"

    const val HOUR24 = "24h"
    const val DAY7 = "7d"
    const val YEAR1 = "1y"
    const val YEAR6 = "5y"

    const val PRICE_FIELD = "price"
    const val VOLUME_FIELD = "24hVolume"
    const val MARKET_CAP_FIELD = "marketCap"
    const val ASC = "asc"
    const val DESC = "desc"
    const val DEFAULT_OFFSET = 0
    const val LIMIT = 50

    const val CHECKED_SORTING_ITEM_INDEX = 4
    const val CHECKED_TIME_PERIOD_ITEM_INDEX = 1

    const val ROTATE_180 = 180f
    const val ROTATE_360 = 360f

    val filterTags = arrayOf(
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

    val timePeriods = arrayOf("3h", "24h", "7d", "30d", "3m", "1y", "3y", "5y")

    val sortingTags = arrayOf(
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
