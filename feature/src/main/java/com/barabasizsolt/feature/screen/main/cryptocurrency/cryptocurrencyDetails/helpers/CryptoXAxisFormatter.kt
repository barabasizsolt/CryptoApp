package com.barabasizsolt.feature.screen.main.cryptocurrency.cryptocurrencyDetails.helpers

import com.barabasizsolt.feature.screen.main.cryptocurrency.Constant.daysOfWeek
import com.barabasizsolt.feature.screen.main.cryptocurrency.Constant.monthsOfYear
import com.barabasizsolt.feature.shared.utils.formatHour
import com.barabasizsolt.feature.shared.utils.formatYear
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class CryptoXAxisFormatter(private val unitOfTimeType: UnitOfTimeType) : ValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase?) = when (unitOfTimeType) {
        UnitOfTimeType.UNIT_24H -> value.formatHour()
        UnitOfTimeType.UNIT_7D -> daysOfWeek[value]
        UnitOfTimeType.UNIT_1Y -> monthsOfYear[value]
        UnitOfTimeType.UNIT_MAX -> value.formatYear()
    }
}
