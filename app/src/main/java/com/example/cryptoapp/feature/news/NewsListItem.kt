package com.example.cryptoapp.feature.news

import com.example.cryptoapp.feature.shared.ListItem
import com.example.cryptoapp.feature.shared.getFormattedTime

sealed class NewsListItem : ListItem {

    data class ErrorState(
        val nothing: Any? = null
    ) : NewsListItem() {
        override val id = "errorState"
    }

    data class LoadMore(
        val nothing: Any? = null
    ) : NewsListItem() {
        override val id = "loadMore"
    }

    data class News(
        val title: String,
        val site: String,
        val updated: Long,
        val url: String,
        val logo: String
    ) : NewsListItem() {
        override val id = "news_$title"
        val source = site + " (" + updated.getFormattedTime(withHours = true) + ")"
    }
}
