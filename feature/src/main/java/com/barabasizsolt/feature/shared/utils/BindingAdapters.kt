package com.barabasizsolt.feature.shared.utils

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.barabasizsolt.feature.R
import com.barabasizsolt.feature.screen.main.cryptocurrency.cryptocurrencyDetails.helpers.CryptoXAxisFormatter
import com.barabasizsolt.feature.screen.main.cryptocurrency.cryptocurrencyDetails.helpers.CryptoYAxisFormatter
import com.barabasizsolt.feature.screen.main.cryptocurrency.cryptocurrencyDetails.helpers.UnitOfTimeType
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.color.MaterialColors


@BindingAdapter("percentage")
fun TextView.setPercentage(percentageStr: String) {
    val percentage = percentageStr.toDouble()
    when {
        percentage < 0 -> {
            val percentageText = String.format("%.2f", percentage) + "%"
            text = percentageText
            setTextColor(ContextCompat.getColor(context, R.color.red))
        }
        percentage > 0 -> {
            val percentageText = "+" + String.format("%.2f", percentage) + "%"
            text = percentageText
            setTextColor(ContextCompat.getColor(context, R.color.green))
        }
    }
}

@BindingAdapter("url")
fun ImageView.loadImage(url: String?) = ImageLoader.Builder(context)
    .componentRegistry { add(SvgDecoder(context)) }
    .build()
    .enqueue(
        ImageRequest.Builder(context)
            .data(url)
            .target(this)
            .build()
    )

@BindingAdapter("data", "formatter", requireAll = true)
fun LineChart.initializeCryptoCurrencyDetailChart(
    dataSet: LineDataSet,
    unitOfTimeType: UnitOfTimeType
) = this.let {
    marker = object : MarkerView(context, R.layout.chart_marker_view) {

        private var txtViewData: TextView = findViewById(R.id.marker_view)

        override fun refreshContent(entry: Entry?, highlight: Highlight?) {
            val value = entry?.y?.toDouble() ?: 0.0
            txtViewData.text = value.toString().convertToPrice()
            super.refreshContent(entry, highlight)
        }

        override fun getOffset(): MPPointF = MPPointF(-(width / 2f), -height.toFloat())
    }

    extraBottomOffset = 5f
    setTouchEnabled(true)
    isDragEnabled = true
    setScaleEnabled(true)
    setPinchZoom(false)
    setDrawGridBackground(false)
    description.isEnabled = false
    legend.isEnabled = true
    legend.textColor = MaterialColors.getColor(context, R.attr.colorOnSurface, Color.WHITE)
    legend.textSize = 13f
    legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
    legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
    legend.orientation = Legend.LegendOrientation.HORIZONTAL
    legend.setDrawInside(false)
    legend.setCustom(
        arrayListOf(
            LegendEntry().also {
                it.label = resources.getString(R.string.crypto_value_changes)
                it.formColor = MaterialColors.getColor(context, R.attr.colorOnChart, Color.WHITE)
            }
        )
    )
    xAxis.textColor = MaterialColors.getColor(context, R.attr.colorOnSurface, Color.WHITE)
    xAxis.textSize = 12f
    xAxis.position = XAxis.XAxisPosition.BOTTOM
    xAxis.setDrawGridLines(true)
    xAxis.isGranularityEnabled = true
    xAxis.valueFormatter = CryptoXAxisFormatter(unitOfTimeType = unitOfTimeType)
    axisLeft.textColor = MaterialColors.getColor(context, R.attr.colorOnSurface, Color.WHITE)
    axisLeft.valueFormatter = CryptoYAxisFormatter()
    axisLeft.setDrawGridLines(true)
    axisRight.isEnabled = false
    setBackgroundColor(MaterialColors.getColor(context, R.attr.colorSurface, Color.WHITE))
    dataSet.let {
        it.color = MaterialColors.getColor(context, R.attr.colorOnChart, Color.WHITE)
        it.fillColor = MaterialColors.getColor(context, R.attr.colorOnChart, Color.WHITE)
        it.setDrawHighlightIndicators(true)
        it.highlightLineWidth = 1.5f
        it.enableDashedHighlightLine(10f, 6f, 10f)
        it.highLightColor = Color.BLACK
    }
    data = LineData(arrayListOf<ILineDataSet>(dataSet))
    notifyDataSetChanged()
    invalidate()
}