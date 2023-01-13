package com.barabasizsolt.feature.screen.main.category

import com.barabasizsolt.feature.shared.utils.ListItem


sealed class CategoryListItem : ListItem {

    data class ErrorState(
        val nothing: Any? = null
    ) : CategoryListItem() {
        override val id = "errorState"
    }

    data class Category(
        val categoryId: String,
        val name: String,
        val top3CoinLogos: List<String>,
        val volume: String,
        val marketCap: String,
        val marketCapChange: String
    ) : CategoryListItem() {
        override val id = "categories_$categoryId"
    }
}
