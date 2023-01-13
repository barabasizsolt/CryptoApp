package com.barabasizsolt.feature.shared.utils

interface ListItem {

    val id: String

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}
