package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails.helpers

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlin.math.roundToInt

class CryptoXAxisFormatter(private val axisFormatterType: AxisFormatterType) : ValueFormatter() {

    private val daysOfWeek = mapOf(
        1f to "Mon",
        2f to "Tues",
        3f to "Wed",
        4f to "Thurs",
        5f to "Fri",
        6f to "Sat",
        7f to "Sun"
    )

    private val monthsOfYear = mapOf(
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
        12f to "Dec",
    )

    private fun Float.formatHour() = if (this < 10f) "0${this.toInt()}:00" else "${this.toInt()}:00"

    private fun Float.formatYear() = this.roundToInt().toString()

    override fun getAxisLabel(value: Float, axis: AxisBase?) = when (axisFormatterType) {
        AxisFormatterType.FORMAT_24H -> value.formatHour()
        AxisFormatterType.FORMAT_7D -> daysOfWeek[value]
        AxisFormatterType.FORMAT_1Y -> monthsOfYear[value]
        AxisFormatterType.FORMAT_6Y -> value.formatYear()
    }
}
