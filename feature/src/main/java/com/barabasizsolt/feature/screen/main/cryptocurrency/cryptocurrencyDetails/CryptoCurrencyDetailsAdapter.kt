package com.barabasizsolt.feature.screen.main.cryptocurrency.cryptocurrencyDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.barabasizsolt.feature.R
import com.barabasizsolt.feature.databinding.ItemCryptocurrencyDetailsChartBinding
import com.barabasizsolt.feature.databinding.ItemCryptocurrencyDetailsChipGroupBinding
import com.barabasizsolt.feature.databinding.ItemCryptocurrencyDetailsCoinBodyBinding
import com.barabasizsolt.feature.databinding.ItemCryptocurrencyDetailsCoinHeaderBinding
import com.barabasizsolt.feature.databinding.ItemCryptocurrencyDetailsCoinLogoBinding
import com.barabasizsolt.feature.databinding.ItemCryptocurrencyDetailsErrorStateBinding
import com.barabasizsolt.feature.screen.main.cryptocurrency.cryptocurrencyDetails.helpers.UnitOfTimeType
import com.barabasizsolt.feature.shared.utils.ListItemDiff
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class CryptoCurrencyDetailsAdapter(
    private val onChipClicked: (UnitOfTimeType) -> Unit,
    private val onTryAgainButtonClicked: () -> Unit
) : ListAdapter<CryptoCurrencyDetailsListItem, RecyclerView.ViewHolder>(ListItemDiff()) {
    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is CryptoCurrencyDetailsListItem.CryptoCurrencyLogo -> R.layout.item_cryptocurrency_details_coin_logo
        is CryptoCurrencyDetailsListItem.CryptoCurrencyChart -> R.layout.item_cryptocurrency_details_chart
        is CryptoCurrencyDetailsListItem.CryptoCurrencyChipGroup -> R.layout.item_cryptocurrency_details_chip_group
        is CryptoCurrencyDetailsListItem.CryptoCurrencyHeader -> R.layout.item_cryptocurrency_details_coin_header
        is CryptoCurrencyDetailsListItem.CryptoCurrencyBody -> R.layout.item_cryptocurrency_details_coin_body
        is CryptoCurrencyDetailsListItem.ErrorState -> R.layout.item_cryptocurrency_details_error_state
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
            parent = parent
        )
        R.layout.item_cryptocurrency_details_error_state -> ErrorStateViewHolder.create(
            parent = parent,
            onTryAgainButtonClicked = onTryAgainButtonClicked
        )
        else -> throw IllegalStateException("Invalid view type: $viewType.")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (val uiModel = getItem(position)) {
        is CryptoCurrencyDetailsListItem.CryptoCurrencyLogo -> (holder as LogoViewHolder).bind(uiModel)
        is CryptoCurrencyDetailsListItem.CryptoCurrencyChart -> (holder as ChartViewHolder).bind(uiModel)
        is CryptoCurrencyDetailsListItem.CryptoCurrencyChipGroup -> (holder as ChipGroupViewHolder).bind(uiModel)
        is CryptoCurrencyDetailsListItem.CryptoCurrencyHeader -> (holder as HeaderViewHolder).bind(uiModel)
        is CryptoCurrencyDetailsListItem.CryptoCurrencyBody -> (holder as BodyViewHolder).bind(uiModel)
        is CryptoCurrencyDetailsListItem.ErrorState -> (holder as ErrorStateViewHolder).bind(uiModel)
    }

    class ErrorStateViewHolder private constructor(
        private val binding: ItemCryptocurrencyDetailsErrorStateBinding,
        private val onTryAgainButtonClicked: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.errorStateLayout.tryAgain.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onTryAgainButtonClicked()
                }
            }
        }

        fun bind(listItem: CryptoCurrencyDetailsListItem.ErrorState) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(parent: ViewGroup, onTryAgainButtonClicked: () -> Unit) = ErrorStateViewHolder(
                binding = ItemCryptocurrencyDetailsErrorStateBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onTryAgainButtonClicked = onTryAgainButtonClicked
            )
        }
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
        private val onChipClicked: (UnitOfTimeType) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val cryptoCurrencyDetailsChipAdapter = CryptoCurrencyDetailsChipAdapter(onChipClicked = onChipClicked)

        init {
            binding.chipRecyclerview.layoutManager = FlexboxLayoutManager(binding.root.context).also {
                it.justifyContent = JustifyContent.SPACE_AROUND
                it.alignItems = AlignItems.CENTER
                it.flexDirection = FlexDirection.ROW
                it.flexWrap = FlexWrap.WRAP
            }
            binding.chipRecyclerview.adapter = cryptoCurrencyDetailsChipAdapter
        }

        fun bind(listItem: CryptoCurrencyDetailsListItem.CryptoCurrencyChipGroup) {
            binding.uiModel = listItem
            cryptoCurrencyDetailsChipAdapter.submitList(listItem.chips)
        }

        companion object {
            fun create(parent: ViewGroup, onChipClicked: (UnitOfTimeType) -> Unit) = ChipGroupViewHolder(
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
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listItem: CryptoCurrencyDetailsListItem.CryptoCurrencyBody) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(parent: ViewGroup) = BodyViewHolder(
                binding = ItemCryptocurrencyDetailsCoinBodyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }
}
