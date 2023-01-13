package com.barabasizsolt.feature.screen.main.cryptocurrency.cryptocurrencyDetails.helpers

import com.barabasizsolt.feature.shared.utils.Constant
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class CryptoYAxisFormatter : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return Constant.numberFormatter.format(value)
    }
}
