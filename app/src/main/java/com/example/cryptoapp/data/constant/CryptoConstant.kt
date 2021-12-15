package com.example.cryptoapp.data.constant

import android.icu.text.CompactDecimalFormat
import android.icu.text.CompactDecimalFormat.CompactStyle
import android.icu.util.CurrencyAmount
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.cryptoapp.R
import com.example.cryptoapp.data.model.cryptoCurrency.CryptoCurrency
import com.example.cryptoapp.data.model.cryptoCurrency.CryptoCurrencyResponse
import com.example.cryptoapp.data.model.cryptoCurrency.CryptoCurrencyUIModel
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.CoinDetailsResponse
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.details.CryptoCurrencyDetails
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.details.CryptoCurrencyDetailsInfoUIModel
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.details.CryptoCurrencyDetailsUIModel
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.history.*
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Currency
import java.util.Locale

object CryptoConstant {
    private const val COUNTRY = "US"
    private const val LANGUAGE = "en"
    private val format: CompactDecimalFormat =
        CompactDecimalFormat.getInstance(Locale.US, CompactStyle.SHORT)
    private val numberFormat: NumberFormat =
        NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY))
    private val currency = Currency.getInstance(Locale.US)

    val CALENDAR: Calendar = Calendar.getInstance()
    const val MAX_HOUR: Int = 24
    const val MAX_DAY: Int = 7
    const val MAX_MONTH: Int = 12

    init {
        format.maximumFractionDigits = 2
    }

    private const val EMPTY_STR = "null"

    const val CURRENCY_FIRE_STORE_PATH = "currencies"

    const val COIN_ID: String = "coin_id"

    const val DEFAULT_VOLUME = "24H"

    const val HOUR24 = "24h"
    const val DAY7 = "7d"
    const val YEAR1 = "1y"
    const val YEAR6 = "5y"

    const val PRICE_FIELD = "price"
    const val VOLUME_FIELD = "24hVolume"
    const val MARKET_CAP_FIELD = "marketCap"
    const val ASC = "asc"
    const val DESC = "desc"
    const val OFFSET = 0
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

    fun ImageView.loadSvg(url: String) {
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
            .build()
        val request = ImageRequest.Builder(this.context)
            .data(url)
            .target(this)
            .build()
        imageLoader.enqueue(request)
    }

    fun ImageView.loadImage(image: Uri, placeholder: Int) {
        this.load(image) {
            placeholder(placeholder)
            transformations(CircleCropTransformation())
        }
    }

    fun ImageView.loadImage(image: String, placeholder: Int) {
        this.load(image) {
            placeholder(placeholder)
            transformations(CircleCropTransformation())
        }
    }

    fun ImageView.loadImage(image: Int) {
        this.load(image) {
            transformations(CircleCropTransformation())
        }
    }

    fun setPercentage(percentageStr: String, textView: TextView) {
        if (percentageStr == EMPTY_STR) return
        val percentage = percentageStr.toDouble()
        when {
            percentage < 0 -> {
                val percentageText = String.format("%.2f", percentage) + "%"
                textView.text = percentageText
                textView.setTextColor(ContextCompat.getColor(textView.context, R.color.red))
            }
            percentage > 0 -> {
                val percentageText = "+" + String.format("%.2f", percentage) + "%"
                textView.text = percentageText
                textView.setTextColor(ContextCompat.getColor(textView.context, R.color.green))
            }
        }
    }

    fun getTime(timeStamp: Long): LocalDateTime =
        Instant.ofEpochSecond(timeStamp).atZone(ZoneId.systemDefault()).toLocalDateTime()

    fun getFormattedTime(timeStamp: Long): String {
        val time = getTime(timeStamp)
        val month = time.month.name.lowercase().replaceFirstChar { it.uppercase() }.substring(0, 3)
        return month + " " + time.dayOfMonth.toString() + ", " + time.year.toString()
    }

    fun setValue(inputNumber: String): String {
        if (inputNumber == "null") return "undefined"
        return format.format(inputNumber.toDouble())
    }

    fun setPrice(inputNumber: String): String {
        if (inputNumber == "null") return "undefined"
        return numberFormat.format(inputNumber.toDouble())
    }

    fun setCompactPrice(inputNumber: String): String {
        if (inputNumber == "null") return "undefined"
        val amount = CurrencyAmount(inputNumber.toDouble(), currency)
        return format.format(amount)
    }

    fun CryptoCurrencyResponse.toCryptoCurrency() = CryptoCurrency(
        uuid = uuid,
        symbol = symbol.toString(),
        name = name.toString(),
        iconUrl = iconUrl.toString(),
        marketCap = marketCap.toString(),
        price = price.toString(),
        change = change.toString(),
        volume = volume.toString(),
    )

    fun CryptoCurrency.toCryptoCurrencyUIModel(timePeriod: String) = CryptoCurrencyUIModel(
        cryptoCurrency = this,
        timePeriod = timePeriod.uppercase(Locale.getDefault())
    )

    fun CoinDetailsResponse.toCryptoCurrencyDetails() = CryptoCurrencyDetails(
        uuid = uuid,
        symbol = symbol,
        name = name,
        iconUrl = iconUrl,
        marketCap = marketCap,
        price = price,
        change = change,
        volume = volume,
        rank = rank,
        totalSupply = supply.total,
        circulating = supply.circulating,
        btcPrice = btcPrice,
        allTimeHigh = allTimeHigh,
        description = description
    )

    fun CryptoCurrencyDetails.toCryptoCurrencyDetailsUIModel() = CryptoCurrencyDetailsUIModel(
        uuid = uuid.toString(),
        symbol = symbol.toString(),
        name = name.toString(),
        iconUrl = iconUrl.toString(),
        marketCap = marketCap.toString(),
        price = price.toString(),
        change = change.toString(),
        volume = volume.toString()
    )

    fun CryptoCurrencyDetails.toCryptoCurrencyDetailsInfoUIModel() = CryptoCurrencyDetailsInfoUIModel(
        rank = rank.toString(),
        totalSupply = totalSupply.toString(),
        circulating = circulating.toString(),
        btcPrice = btcPrice.toString(),
        allTimeHigh = allTimeHigh,
        description = description.toString()
    )

    fun CryptoCurrencyHistoryResponse.toCryptoCurrencyHistory() = CryptoCurrencyHistory(
        history = data.history
    )

    fun CryptoCurrencyHistory.toCryptoCurrencyHistoryUIModel() = CryptoCurrencyHistoryUIModel(
        history = history as MutableList
    )
}
