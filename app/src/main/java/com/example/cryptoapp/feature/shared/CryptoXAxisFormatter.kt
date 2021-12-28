package com.example.cryptoapp.feature.shared

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class CryptoXAxisFormatter: ValueFormatter() {

    private val formatter: SimpleDateFormat = SimpleDateFormat("h:mm", Locale.getDefault())

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return formatter.format(Date(value))
    }
}