package com.example.cryptoapp.wear.common

import android.icu.text.CompactDecimalFormat
import android.icu.util.CurrencyAmount
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val COUNTRY = "US"
private const val LANGUAGE = "en"

private val formatter = CompactDecimalFormat.getInstance(Locale.US, CompactDecimalFormat.CompactStyle.SHORT).apply { maximumFractionDigits = 2 }
private val numberFormatter = NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY))
private val currency = Currency.getInstance(Locale.US)
private val hourFormatter = SimpleDateFormat("h:mm", Locale(LANGUAGE, COUNTRY))

fun Long.getFormattedHour(): String = hourFormatter.format(this)

fun String.formatInput(): String = formatter.format(this.toDouble())

fun String.convertToPrice(): String = numberFormatter.format(this.toDouble())

fun String.convertToCompactPrice(): String = formatter.format(CurrencyAmount(this.toDouble(), currency))

fun String.isSvg(): Boolean = this.contains(".svg")

class Event<T : Any>(private val data: T) {

    private var consumed: Boolean = false

    fun consume(): T? = data.takeUnless { consumed }?.also { consumed = true }

    fun peek() = data
}
