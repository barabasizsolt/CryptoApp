package com.example.cryptoapp.feature.shared

import android.content.Context
import android.icu.util.CurrencyAmount
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.cryptoapp.R
import com.example.cryptoapp.data.constant.CryptoConstant
import com.example.cryptoapp.feature.shared.Constant.currency
import com.example.cryptoapp.feature.shared.Constant.formatter
import com.example.cryptoapp.feature.shared.Constant.numberFormatter
import com.google.android.material.color.MaterialColors
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@BindingAdapter("percentage")
fun TextView.setPercentage(percentageStr: String?) {
    if (percentageStr == CryptoConstant.EMPTY_STR || percentageStr.isNullOrBlank()) return
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

fun Long.getFormattedTime(): String {
    val time = this.getTime()
    val month = time.month.name.lowercase().replaceFirstChar { it.uppercase() }.substring(0, 3)
    return month + " " + time.dayOfMonth.toString() + ", " + time.year.toString()
}

fun String.formatInput(): String =
    if (this == "null") "undefined" else formatter.format(this.toDouble())

fun String.convertToPrice(): String =
    if (this == "null") "undefined" else numberFormatter.format(this.toDouble())

fun String.convertToCompactPrice(): String =
    if (this == "null") "undefined" else formatter.format(CurrencyAmount(this.toDouble(), currency))

fun Int.getColorFromAttr(context: Context, defaultColor: Int): Int = MaterialColors.getColor(context, this, defaultColor)

fun Int.toHexStringColor(): String = "#" + Integer.toHexString(this).substring(2)
