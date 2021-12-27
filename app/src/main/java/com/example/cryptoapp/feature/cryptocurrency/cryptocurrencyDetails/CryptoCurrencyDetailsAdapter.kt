package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.*
import com.example.cryptoapp.feature.shared.ListItemDiff

class CryptoCurrencyDetailsAdapter(
    private val onChipClicked: (ChipType) -> Unit,
    private val onDescriptionArrowClicked: (ImageView, TextView) -> Unit
) : ListAdapter<CryptoCurrencyDetailsListItem, RecyclerView.ViewHolder>(ListItemDiff()) {
    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is CryptoCurrencyDetailsListItem.CryptoCurrencyLogo -> R.layout.item_cryptocurrency_details_coin_logo
        is CryptoCurrencyDetailsListItem.CryptoCurrencyChart -> R.layout.item_cryptocurrency_details_chart
        is CryptoCurrencyDetailsListItem.CryptoCurrencyChipGroup -> R.layout.item_cryptocurrency_details_chip_group
        is CryptoCurrencyDetailsListItem.CryptoCurrencyHeader -> R.layout.item_cryptocurrency_details_coin_header
        is CryptoCurrencyDetailsListItem.CryptoCurrencyBody -> R.layout.item_cryptocurrency_details_coin_body
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.item_cryptocurrency_details_coin_logo -> LogoViewHolder.create(
            parent = parent
        )
        R.layout.item_cryptocurrency_details_chart -> ChartViewHolder.create(
            parent = parent
        )
        R.layout.item_cryptocurrency_details_chip_group -> ChipGroupViewHolder.create(
            parent = parent,
            onChipClicked = onChipClicked
        )
        R.layout.item_cryptocurrency_details_coin_header -> HeaderViewHolder.create(
            parent = parent
        )
        R.layout.item_cryptocurrency_details_coin_body -> BodyViewHolder.create(
            parent = parent,
            onDescriptionArrowClicked = onDescriptionArrowClicked
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

        fun bind(listItem: CryptoCurrencyDetailsListItem.CryptoCurrencyLogo) {
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

        fun bind(listItem: CryptoCurrencyDetailsListItem.CryptoCurrencyChart) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(parent: ViewGroup) = ChartViewHolder(
                binding = ItemCryptocurrencyDetailsChartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    class ChipGroupViewHolder private constructor(
        private val binding: ItemCryptocurrencyDetailsChipGroupBinding,
        private val onChipClicked: (ChipType) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.chip24h.setOnClickListener { onChipClicked(ChipType.CHIP_24H) }
            binding.chip7d.setOnClickListener { onChipClicked(ChipType.CHIP_7D) }
            binding.chip1y.setOnClickListener { onChipClicked(ChipType.CHIP_1Y) }
            binding.chip6y.setOnClickListener { onChipClicked(ChipType.CHIP_6Y) }
        }

        fun bind(listItem: CryptoCurrencyDetailsListItem.CryptoCurrencyChipGroup) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(parent: ViewGroup, onChipClicked: (ChipType) -> Unit) = ChipGroupViewHolder(
                binding = ItemCryptocurrencyDetailsChipGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onChipClicked = onChipClicked
            )
        }
    }

    class HeaderViewHolder private constructor(
        private val binding: ItemCryptocurrencyDetailsCoinHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listItem: CryptoCurrencyDetailsListItem.CryptoCurrencyHeader) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(parent: ViewGroup) = HeaderViewHolder(
                binding = ItemCryptocurrencyDetailsCoinHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    class BodyViewHolder private constructor(
        private val binding: ItemCryptocurrencyDetailsCoinBodyBinding,
        private val onDescriptionArrowClicked: (ImageView, TextView) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.descriptionDropDown.setOnClickListener{ onDescriptionArrowClicked(it as ImageView, binding.cryptoDescriptionText) }
        }

        fun bind(listItem: CryptoCurrencyDetailsListItem.CryptoCurrencyBody) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(parent: ViewGroup, onDescriptionArrowClicked: (ImageView, TextView) -> Unit) = BodyViewHolder(
                binding = ItemCryptocurrencyDetailsCoinBodyBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onDescriptionArrowClicked = onDescriptionArrowClicked
            )
        }
    }
}
