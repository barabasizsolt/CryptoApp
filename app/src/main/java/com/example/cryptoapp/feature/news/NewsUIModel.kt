package com.example.cryptoapp.feature.news
import com.example.cryptoapp.feature.shared.getFormattedTime

data class NewsUIModel(
    val title: String,
    val site: String,
    val updated: Long,
    val logo: String
) {
    val source = site + " (" + updated.getFormattedTime(withHours = true) + ")"
}
