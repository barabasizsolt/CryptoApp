package com.example.cryptoapp.feature.shared

import android.content.Context
import android.icu.util.CurrencyAmount
import android.net.Uri
import android.util.Log
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
import com.anychart.anychart.*
import com.anychart.anychart.AnyChart.area
import com.example.cryptoapp.R
import com.example.cryptoapp.feature.shared.Constant.currency
import com.example.cryptoapp.feature.shared.Constant.formatter
import com.example.cryptoapp.feature.shared.Constant.numberFormatter
import com.google.android.material.color.MaterialColors
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
    val hour = if (time.hour < 10) "0${time.hour}" else time.hour
    val minute = if (time.minute < 10) "0${time.minute}" else time.minute

    return when (withHours) {
        true -> month + " " + time.dayOfMonth.toString() + ", " + time.year.toString() + " at " + hour + ":" + minute
        else -> month + " " + time.dayOfMonth.toString() + ", " + time.year.toString()
    }
}

fun String.formatInput(): String = formatter.format(this.toDouble())

fun String.convertToPrice(): String = numberFormatter.format(this.toDouble())

fun String.convertToCompactPrice(): String = formatter.format(CurrencyAmount(this.toDouble(), currency))

fun Int.getColorFromAttr(context: Context, defaultColor: Int): Int = MaterialColors.getColor(context, this, defaultColor)

fun Int.toHexStringColor(): String = "#" + Integer.toHexString(this).substring(2)

fun View.createErrorSnackBar(errorMessage: String, snackBarAction: () -> Unit) =
    Snackbar.make(this, errorMessage, Snackbar.LENGTH_LONG)
        .setAction(resources.getString(R.string.retry)) { snackBarAction() }
        .show()

@BindingAdapter("data", "bgColor", "txtColor", "cColor",  requireAll = true)
fun AnyChartView.initializeChart(data: MutableList<DataEntry>, chartBackgroundColor: String, chartTextColor: String, chartColor: String) {
    this.setBackgroundColor(chartBackgroundColor)

    val areaChart: Cartesian = area()
    val series = areaChart.area(data)
    with(series) {
        setName("Cryptocurrency History")
        setStroke("1 $chartTextColor")
        hovered.markers.setEnabled(true)
        series.markers.setZIndex(100.0)
        hovered.setStroke("3 $chartTextColor")
        fill(chartColor, 5)
    }
    with(series.hovered.markers) {
        setType(MarkerType.CIRCLE)
        setSize(4.0)
        setStroke("1.5 $chartTextColor")
    }
    areaChart.setData(data)

    with(areaChart) {
        yScale.setStackMode(ScaleStackMode.VALUE)
        yGrid.setEnabled(true)
        background.fill(chartBackgroundColor, 0)
        interactivity.setHoverMode(HoverMode.BY_X)
    }
    with(areaChart.crosshair) {
        setEnabled(true)
        setYStroke(null as Stroke?, null as Number?, null as String?, null as StrokeLineJoin?, null as StrokeLineCap?)
        setXStroke(chartTextColor, 1.0, null, null as StrokeLineJoin?, null as StrokeLineCap?)
        setZIndex(39.0)
        getYLabel(0).setEnabled(true)
    }
    with(areaChart.tooltip) {
        setValuePrefix("$")
        setDisplayMode(TooltipDisplayMode.SINGLE)
    }
    with(areaChart.legend) {
        setEnabled(true)
        setFontSize(13.0)
        setPadding(0.0, 0.0, 20.0, 0.0)
    }
    with(areaChart.getXAxis(0)) {
        setTitle(false)
        labels.setFontColor(chartTextColor)
    }
    with(areaChart.getYAxis(0)) {
        setTitle("Value (in US Dollars)")
        title.setFontColor(chartTextColor)
        labels.setFormat("\${%value}")
        labels.setFontColor(chartTextColor)
    }
    this.setChart(areaChart)
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
