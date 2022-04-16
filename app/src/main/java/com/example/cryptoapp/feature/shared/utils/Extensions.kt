package com.example.cryptoapp.feature.shared.utils

import android.icu.text.SimpleDateFormat
import android.icu.util.CurrencyAmount
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.cryptoapp.R
import com.example.cryptoapp.data.model.cryptocurrency.CryptoCurrencyHistory
import com.example.cryptoapp.feature.main.cryptocurrency.Constant
import com.example.cryptoapp.feature.shared.utils.Constant.currency
import com.example.cryptoapp.feature.shared.utils.Constant.formatter
import com.example.cryptoapp.feature.shared.utils.Constant.hourFormatter
import com.example.cryptoapp.feature.shared.utils.Constant.numberFormatter
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.snackbar.Snackbar
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.util.Locale
import kotlin.collections.ArrayList

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

fun Long.formatUserRegistrationDate(): String = SimpleDateFormat("MMM dd, yyy", Locale.getDefault()).format(this)

fun String.formatInput(): String = formatter.format(this.toDouble())

fun String.convertToPrice(): String = numberFormatter.format(this.toDouble())

fun String.convertToCompactPrice(): String = formatter.format(CurrencyAmount(this.toDouble(), currency))

fun String.isSvg(): Boolean = this.contains(".svg")

fun Int.ordinalOf() = "$this" + if (this % 100 in 11..13) "th" else when (this % 10) {
    1 -> "st"
    2 -> "nd"
    3 -> "rd"
    else -> "th"
}

fun View.createErrorSnackBar(errorMessage: String, snackBarAction: () -> Unit) =
    Snackbar.make(this, errorMessage, Snackbar.LENGTH_LONG)
        .setAction(resources.getString(R.string.retry)) { snackBarAction() }
        .show()

fun View.createErrorSnackBar(errorMessage: String) =
    Snackbar.make(this, errorMessage, Snackbar.LENGTH_LONG)
        .show()

private fun List<CryptoCurrencyHistory>.toChartArray(timePeriod: String): ArrayList<Entry> {
    val currencyHistory: ArrayList<Entry> = ArrayList()
    when (timePeriod) {
        Constant.HOUR24 -> {
            val groupedHistory = sortedMapOf<Int, MutableList<Double>>()

            this.forEach { curr ->
                val time = curr.timestamp.getTime().hour
                if (!groupedHistory.containsKey(time)) {
                    groupedHistory[time] = mutableListOf()
                }
                groupedHistory[time]?.add(curr.price.toDouble())
            }

            groupedHistory.forEach { elem ->
                currencyHistory.add(Entry(elem.key.toFloat(), elem.value.average().toFloat()))
            }
        }
        Constant.DAY7 -> {
            val groupedHistory = mutableMapOf<DayOfWeek, MutableList<Double>>()

            this.forEach { curr ->
                val dayOfWeek = curr.timestamp.getTime().dayOfWeek
                if (!groupedHistory.containsKey(dayOfWeek)) {
                    groupedHistory[dayOfWeek] = mutableListOf()
                }
                groupedHistory[dayOfWeek]?.add(curr.price.toDouble())
            }

            groupedHistory.toSortedMap(compareBy { it.ordinal }).forEach { elem ->
                currencyHistory.add(Entry(elem.key.value.toFloat(), elem.value.average().toFloat()))
            }
        }
        Constant.YEAR1 -> {
            val groupedHistory = mutableMapOf<Month, MutableList<Double>>()

            this.forEach { curr ->
                val month = curr.timestamp.getTime().month
                if (!groupedHistory.containsKey(month)) {
                    groupedHistory[month] = mutableListOf()
                }
                groupedHistory[month]?.add(curr.price.toDouble())
            }

            groupedHistory.toSortedMap(compareBy { it.ordinal }).forEach { elem ->
                currencyHistory.add(Entry(elem.key.value.toFloat(), elem.value.average().toFloat()))
            }
        }
        Constant.YEAR6 -> {
            val groupedHistory = mutableMapOf<Int, MutableList<Double>>()

            this.forEach { curr ->
                val year = curr.timestamp.getTime().year
                if (!groupedHistory.containsKey(year)) {
                    groupedHistory[year] = mutableListOf()
                }
                groupedHistory[year]?.add(curr.price.toDouble())
            }
            groupedHistory.toSortedMap(compareBy { it }).forEach { elem ->
                currencyHistory.add(Entry(elem.key.toFloat(), elem.value.average().toFloat()))
            }
        }
    }

    return currencyHistory
}

fun List<CryptoCurrencyHistory>.toChartDataSet(timePeriod: String) = LineDataSet(this.toChartArray(timePeriod = timePeriod), "data")
    .also { lineDataSet ->
        lineDataSet.lineWidth = 3f
        lineDataSet.setDrawValues(false)
        lineDataSet.circleRadius = 10f
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet.cubicIntensity = 0.1f
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillAlpha = 255
        lineDataSet.setDrawCircles(false)
    }

inline fun <reified T : Fragment> FragmentManager.handleReplace(
    tag: String = T::class.java.name,
    addToBackStack: Boolean = false,
    @IdRes containerId: Int = R.id.fragment_container,
    crossinline newInstance: () -> T
) {
    beginTransaction().apply {
        val currentFragment = findFragmentById(containerId)
        // val newFragment = findFragmentByTag(tag) ?: newInstance()
        val newFragment = newInstance()

        // TODO [mid] add better transition -> crashes compose
//        currentFragment?.let {
//            currentFragment.exitTransition = MaterialFadeThrough()
//            currentFragment.reenterTransition = MaterialFadeThrough()
//            newFragment.enterTransition = MaterialFadeThrough()
//            newFragment.returnTransition = MaterialFadeThrough()
//        }

        replace(containerId, newFragment, tag)
        if (addToBackStack) {
            addToBackStack(tag)
        }
        setReorderingAllowed(true)
        commitAllowingStateLoss()
    }
}
