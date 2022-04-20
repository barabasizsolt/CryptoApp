package com.example.cryptoapp.feature.screen.main.cryptocurrency
import java.util.Calendar

object Constant {
    const val ROTATE_180 = 180f
    const val ROTATE_360 = 360f

    private const val PRICE_FIELD = "price"
    private const val VOLUME_FIELD = "24hVolume"
    private const val MARKET_CAP_FIELD = "marketCap"
    private const val ASC = "asc"
    private const val DESC = "desc"

    const val CURRENCY_FIRE_STORE_PATH = "currencies"
    const val COIN_ID: String = "coin_id"

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

    val daysOfWeek = mapOf(
        1f to "Mon",
        2f to "Tues",
        3f to "Wed",
        4f to "Thurs",
        5f to "Fri",
        6f to "Sat",
        7f to "Sun"
    )

    val monthsOfYear = mapOf(
        1f to "Jan",
        2f to "Feb",
        3f to "Mar",
        4f to "Apr",
        5f to "May",
        6f to "June",
        7f to "July",
        8f to "Aug",
        9f to "Sept",
        10f to "Oct",
        11f to "Nov",
        12f to "Dec"
    )
}
