package com.example.cryptoapp.feature.shared

import android.icu.text.CompactDecimalFormat
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object Constant {
    private const val COUNTRY = "US"
    private const val LANGUAGE = "en"
    val formatter: CompactDecimalFormat =
        CompactDecimalFormat.getInstance(Locale.US, CompactDecimalFormat.CompactStyle.SHORT)
    val numberFormatter: NumberFormat =
        NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY))
    val currency: Currency = Currency.getInstance(Locale.US)

    init {
        formatter.maximumFractionDigits = 2
    }
}
