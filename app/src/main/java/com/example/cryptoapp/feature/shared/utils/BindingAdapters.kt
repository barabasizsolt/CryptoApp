package com.example.cryptoapp.feature.shared.utils

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.cryptoapp.R
import com.example.cryptoapp.data.model.auth.UserAvatarType
import com.example.cryptoapp.feature.main.cryptocurrency.cryptocurrencyDetails.helpers.CryptoXAxisFormatter
import com.example.cryptoapp.feature.main.cryptocurrency.cryptocurrencyDetails.helpers.CryptoYAxisFormatter
import com.example.cryptoapp.feature.main.cryptocurrency.cryptocurrencyDetails.helpers.UnitOfTimeType
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
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
fun LineChart.initializeChart(
    dataSet: LineDataSet,
    unitOfTimeType: UnitOfTimeType
) = this.let {
    extraBottomOffset = 5f
    setTouchEnabled(false)
    isDragEnabled = true
    setScaleEnabled(true)
    setPinchZoom(false)
    setDrawGridBackground(false)
    description.isEnabled = false
    legend.isEnabled = true
    legend.textColor = MaterialColors.getColor(context, R.attr.app_text_color, Color.WHITE)
    legend.textSize = 13f
    legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
    legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
    legend.orientation = Legend.LegendOrientation.HORIZONTAL
    legend.setDrawInside(false)
    legend.setCustom(
        arrayListOf(
            LegendEntry().also {
                it.label = resources.getString(R.string.crypto_value_changes)
                it.formColor = MaterialColors.getColor(context, R.attr.crypto_chart_color, Color.WHITE)
            }
        )
    )
    xAxis.textColor = MaterialColors.getColor(context, R.attr.app_text_color, Color.WHITE)
    xAxis.textSize = 12f
    xAxis.position = XAxis.XAxisPosition.BOTTOM
    xAxis.setDrawGridLines(true)
    xAxis.isGranularityEnabled = true
    axisLeft.textColor = MaterialColors.getColor(context, R.attr.app_text_color, Color.WHITE)
    axisLeft.valueFormatter = CryptoYAxisFormatter()
    axisLeft.setDrawGridLines(true)
    axisRight.isEnabled = false
    setBackgroundColor(MaterialColors.getColor(context, R.attr.app_background_color, Color.WHITE))
    dataSet.let {
        it.color = MaterialColors.getColor(context, R.attr.app_text_color, Color.WHITE)
        it.highLightColor = MaterialColors.getColor(context, R.attr.crypto_chart_color, Color.WHITE)
        it.fillColor = MaterialColors.getColor(context, R.attr.crypto_chart_color, Color.WHITE)
    }
    xAxis.valueFormatter = CryptoXAxisFormatter(unitOfTimeType = unitOfTimeType)
    data = LineData(arrayListOf<ILineDataSet>(dataSet))
    notifyDataSetChanged()
    invalidate()
}

@BindingAdapter("isVisible")
fun View.isVisible(isVisible: Boolean) {
    if (isVisible) this.visibility = View.VISIBLE else this.visibility = View.GONE
}

@BindingAdapter("isEnabled")
fun View.isEnabled(isEnabled: Boolean) {
    this.isEnabled = isEnabled
}

@BindingAdapter("loadAvatar")
fun ImageView.loadAvatar(avatarType: UserAvatarType) = ImageLoader.Builder(context)
    .componentRegistry { add(SvgDecoder(context)) }
    .build()
    .enqueue(
        ImageRequest.Builder(context)
            .data(
                when (avatarType) {
                    is UserAvatarType.IntType -> avatarType.id
                    is UserAvatarType.UriType -> avatarType.uri
                }
            )
            .target(this)
            .build()
    )
