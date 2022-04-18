package com.example.cryptoapp.feature.main.cryptocurrency.cryptocurrencyDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.databinding.ItemCryptocurrencyChipBinding
import com.example.cryptoapp.feature.main.cryptocurrency.cryptocurrencyDetails.helpers.UnitOfTimeType
import com.example.cryptoapp.feature.shared.utils.ChipItem
import com.example.cryptoapp.feature.shared.utils.ListItemDiff

class CryptoCurrencyDetailsChipAdapter(
    private val onChipClicked: (UnitOfTimeType) -> Unit
) : ListAdapter<ChipItem.CryptoCurrencyDetailsChipItem, CryptoCurrencyDetailsChipAdapter.ChipViewHolder>(ListItemDiff()) {

    override fun getItemViewType(position: Int) = when (val chipItemId = getItem(position).chipItemId) {
        0 -> UnitOfTimeType.UNIT_24H.ordinal
        1 -> UnitOfTimeType.UNIT_7D.ordinal
        2 -> UnitOfTimeType.UNIT_1Y.ordinal
        3 -> UnitOfTimeType.UNIT_MAX.ordinal
        else -> throw IllegalStateException("Invalid chipItemId: $chipItemId.")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        UnitOfTimeType.UNIT_24H.ordinal -> ChipViewHolder.create(
            parent = parent,
            onChipClicked = onChipClicked,
            unitOfTimeType = UnitOfTimeType.UNIT_24H
        )
        UnitOfTimeType.UNIT_7D.ordinal -> ChipViewHolder.create(
            parent = parent,
            onChipClicked = onChipClicked,
            unitOfTimeType = UnitOfTimeType.UNIT_7D
        )
        UnitOfTimeType.UNIT_1Y.ordinal -> ChipViewHolder.create(
            parent = parent,
            onChipClicked = onChipClicked,
            unitOfTimeType = UnitOfTimeType.UNIT_1Y
        )
        UnitOfTimeType.UNIT_MAX.ordinal -> ChipViewHolder.create(
            parent = parent,
            onChipClicked = onChipClicked,
            unitOfTimeType = UnitOfTimeType.UNIT_MAX
        )
        else -> throw IllegalStateException("Invalid viewType: $viewType.")
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) = holder.bind(getItem(position))

    class ChipViewHolder private constructor(
        private val binding: ItemCryptocurrencyChipBinding,
        private val onChipClicked: (UnitOfTimeType) -> Unit,
        private val unitOfTimeType: UnitOfTimeType
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.chip.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onChipClicked(unitOfTimeType)
                }
            }
        }

        fun bind(listItem: ChipItem.CryptoCurrencyDetailsChipItem) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(parent: ViewGroup, onChipClicked: (UnitOfTimeType) -> Unit, unitOfTimeType: UnitOfTimeType) = ChipViewHolder(
                binding = ItemCryptocurrencyChipBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onChipClicked = onChipClicked,
                unitOfTimeType = unitOfTimeType
            )
        }
    }
}
