package com.example.cryptoapp.feature.shared

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class CryptoYAxisFormatter: ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return Constant.numberFormatter.format(value)
    }
}