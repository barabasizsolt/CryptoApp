package com.example.cryptoapp.feature.shared

interface ListItem {

    val id: String

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}
