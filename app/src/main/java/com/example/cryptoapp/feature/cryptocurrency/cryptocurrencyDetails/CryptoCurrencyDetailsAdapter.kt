package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.*
import com.example.cryptoapp.feature.shared.ListItemDiff

class CryptoCurrencyDetailsAdapter(

) : ListAdapter<CryptoCurrencyDetailsListItem, RecyclerView.ViewHolder>(ListItemDiff()) {
    override fun getItemViewType(position: Int) = when(getItem(position)) {
        is CryptoCurrencyDetailsListItem.CryptoCurrencyLogo -> R.layout.item_cryptocurrency_details_coin_logo
        is CryptoCurrencyDetailsListItem.CryptoCurrencyChart -> R.layout.item_cryptocurrency_details_chart
        is CryptoCurrencyDetailsListItem.CryptoCurrencyChipGroup -> R.layout.item_cryptocurrency_details_chip_group
        is CryptoCurrencyDetailsListItem.CryptoCurrencyHeader -> R.layout.item_cryptocurrency_details_coin_header
        is CryptoCurrencyDetailsListItem.CryptoCurrencyBody -> R.layout.item_cryptocurrency_details_coin_body
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType) {
        R.layout.item_cryptocurrency_details_coin_logo -> LogoViewHolder.create(
            parent = parent
        )
        R.layout.item_cryptocurrency_details_chart -> ChartViewHolder.create(
            parent = parent
        )
        R.layout.item_cryptocurrency_details_chip_group -> ChipGroupViewHolder.create(
            parent = parent
        )
        R.layout.item_cryptocurrency_details_coin_header -> HeaderViewHolder.create(
            parent = parent
        )
        R.layout.item_cryptocurrency_details_coin_body -> BodyViewHolder.create(
            parent = parent
        )
        else -> throw IllegalStateException("Invalid view type: $viewType.")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (val uiModel = getItem(position)) {
        is CryptoCurrencyDetailsListItem.CryptoCurrencyLogo -> (holder as LogoViewHolder).bind(uiModel)
        is CryptoCurrencyDetailsListItem.CryptoCurrencyChart -> (holder as ChartViewHolder).bind(uiModel)
        is CryptoCurrencyDetailsListItem.CryptoCurrencyChipGroup -> (holder as ChipGroupViewHolder).bind(uiModel)
        is CryptoCurrencyDetailsListItem.CryptoCurrencyHeader -> (holder as HeaderViewHolder).bind(uiModel)
        is CryptoCurrencyDetailsListItem.CryptoCurrencyBody -> (holder as BodyViewHolder).bind(uiModel)
    }

    class LogoViewHolder private constructor(
        private val binding: ItemCryptocurrencyDetailsCoinLogoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listItem: CryptoCurrencyDetailsListItem.CryptoCurrencyLogo){
            binding.uiModel = listItem
        }

        companion object {
            fun create(parent: ViewGroup) = LogoViewHolder(
                binding = ItemCryptocurrencyDetailsCoinLogoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    class ChartViewHolder private constructor(
        private val binding: ItemCryptocurrencyDetailsChartBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listItem: CryptoCurrencyDetailsListItem.CryptoCurrencyChart){
            binding.uiModel = listItem
        }

        companion object {
            fun create(parent: ViewGroup) = ChartViewHolder(
                binding = ItemCryptocurrencyDetailsChartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    class ChipGroupViewHolder private constructor(
        private val binding: ItemCryptocurrencyDetailsChipGroupBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listItem: CryptoCurrencyDetailsListItem.CryptoCurrencyChipGroup){
            binding.uiModel = listItem
        }

        companion object {
            fun create(parent: ViewGroup) = ChipGroupViewHolder(
                binding = ItemCryptocurrencyDetailsChipGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    class HeaderViewHolder private constructor(
        private val binding: ItemCryptocurrencyDetailsCoinHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listItem: CryptoCurrencyDetailsListItem.CryptoCurrencyHeader){
            binding.uiModel = listItem
        }

        companion object {
            fun create(parent: ViewGroup) = HeaderViewHolder(
                binding = ItemCryptocurrencyDetailsCoinHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    class BodyViewHolder private constructor(
        private val binding: ItemCryptocurrencyDetailsCoinBodyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listItem: CryptoCurrencyDetailsListItem.CryptoCurrencyBody){
            binding.uiModel = listItem
        }

        companion object {
            fun create(parent: ViewGroup) = BodyViewHolder(
                binding = ItemCryptocurrencyDetailsCoinBodyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }
}