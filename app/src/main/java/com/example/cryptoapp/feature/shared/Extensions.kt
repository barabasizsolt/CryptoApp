package com.example.cryptoapp.feature.shared

import android.icu.util.CurrencyAmount
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.cryptoapp.R
import com.example.cryptoapp.feature.shared.Constant.currency
import com.example.cryptoapp.feature.shared.Constant.formatter
import com.example.cryptoapp.feature.shared.Constant.hourFormatter
import com.example.cryptoapp.feature.shared.Constant.numberFormatter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

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

fun ImageView.loadImage(image: Uri, placeholder: Int) = load(image) {
    placeholder(placeholder)
    transformations(CircleCropTransformation())
}

fun ImageView.loadImage(image: String, placeholder: Int) = load(image) {
    placeholder(placeholder)
    transformations(CircleCropTransformation())
}

fun ImageView.loadImage(image: Int) = load(image) {
    transformations(CircleCropTransformation())
}

fun Long.getTime(): LocalDateTime =
    Instant.ofEpochSecond(this).atZone(ZoneId.systemDefault()).toLocalDateTime()

fun Long.getFormattedTime(withHours: Boolean = false): String {
    val time = this.getTime()
    val month = time.month.name.lowercase().replaceFirstChar { it.uppercase() }.substring(0, 3)
    val hour = this.getFormattedHour()

    return when (withHours) {
        true -> month + " " + time.dayOfMonth.toString() + ", " + time.year.toString() + " at " + hour
        else -> month + " " + time.dayOfMonth.toString() + ", " + time.year.toString()
    }
}

fun Long.getFormattedHour(): String = hourFormatter.format(this)

fun String.formatInput(): String = formatter.format(this.toDouble())

fun String.convertToPrice(): String = numberFormatter.format(this.toDouble())

fun String.convertToCompactPrice(): String = formatter.format(CurrencyAmount(this.toDouble(), currency))

fun View.createErrorSnackBar(errorMessage: String, snackBarAction: () -> Unit) =
    Snackbar.make(this, errorMessage, Snackbar.LENGTH_LONG)
        .setAction(resources.getString(R.string.retry)) { snackBarAction() }
        .show()

@BindingAdapter("data", "bgColor", "txtColor", "cColor", "isInit", requireAll = true)
fun LineChart.initializeChart(dataSet: LineDataSet, chartBackgroundColor: Int, chartTextColor: Int, chartColor: Int, isChartInitialized: Boolean) =
    this.let {
        if (!isChartInitialized) {
            setTouchEnabled(false)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(false)
            setDrawGridBackground(false)
            description.isEnabled = false
            legend.isEnabled = true
            legend.textColor = chartTextColor
            legend.textSize = 13f
            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.orientation = Legend.LegendOrientation.HORIZONTAL
            legend.setDrawInside(false)
            legend.setCustom(
                arrayListOf(
                    LegendEntry().also {
                        it.label = resources.getString(R.string.crypto_value_changes)
                        it.formColor = chartColor
                    }
                )
            )
            xAxis.textColor = chartTextColor
            xAxis.textSize = 12f
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(true)
            axisLeft.textColor = chartTextColor
            axisLeft.valueFormatter = CryptoYAxisFormatter()
            axisLeft.setDrawGridLines(true)
            axisRight.isEnabled = false
            setBackgroundColor(chartBackgroundColor)
        }
        data = LineData(arrayListOf<ILineDataSet>(dataSet))
        notifyDataSetChanged()
        invalidate()
    }

inline fun <reified T : Fragment> FragmentManager.handleReplace(
    tag: String = T::class.java.name,
    addToBackStack: Boolean = false,
    containerId: Int = R.id.activity_fragment_container,
    crossinline newInstance: () -> T
) {
    beginTransaction().apply {
        val currentFragment = findFragmentById(containerId)
        val newFragment = findFragmentByTag(tag) ?: newInstance()
        currentFragment?.let {
            currentFragment.exitTransition = MaterialFadeThrough()
            currentFragment.reenterTransition = MaterialFadeThrough()
            newFragment.enterTransition = MaterialFadeThrough()
            newFragment.returnTransition = MaterialFadeThrough()
        }
        replace(containerId, newFragment, tag)
        if (addToBackStack) {
            addToBackStack(null)
        }
        setReorderingAllowed(true)
        commitAllowingStateLoss()
    }
}
