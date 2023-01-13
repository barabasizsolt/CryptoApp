package com.barabasizsolt.feature.screen.main.exchange.exchangeDetail.catalog

import com.barabasizsolt.feature.shared.utils.Constant
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class ExchangeYAxisFormatter : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return Constant.formatter.format(value)
    }
}
