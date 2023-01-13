package com.barabasizsolt.feature.screen.main.exchange.exchangeDetail.catalog

import com.barabasizsolt.feature.screen.main.cryptocurrency.Constant.daysOfWeek
import com.barabasizsolt.feature.screen.main.cryptocurrency.cryptocurrencyDetails.helpers.UnitOfTimeType
import com.barabasizsolt.feature.shared.utils.formatHour
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class ExchangeXAxisFormatter(private val unitOfTimeType: UnitOfTimeType) : ValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase?) = when (unitOfTimeType) {
        UnitOfTimeType.UNIT_24H -> value.formatHour()
        UnitOfTimeType.UNIT_7D -> if (value in 1f..7f) daysOfWeek[value] else ""
        else -> ""
    }
}