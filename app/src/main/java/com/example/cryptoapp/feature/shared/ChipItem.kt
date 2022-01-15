package com.example.cryptoapp.feature.shared

sealed class ChipItem : ListItem {

    data class CryptoCurrencyDetailsChipItem(
        val chipItemId: Int,
        val chipTextId: Int,
        val isChecked: Boolean
    ) : ChipItem() {
        override val id = "crypto_details_chip_$chipItemId"
    }
}