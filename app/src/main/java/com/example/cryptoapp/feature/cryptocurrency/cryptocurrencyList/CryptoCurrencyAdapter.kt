package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.databinding.ItemCryptocurrencyCryptoBinding
import com.example.cryptoapp.feature.shared.OnItemClickListener
import com.example.cryptoapp.feature.shared.OnItemLongClickListener

class CryptoCurrencyAdapter(
    private val onItemClickListener: OnItemClickListener,
    private val onItemLongClickListener: OnItemLongClickListener
) : ListAdapter<CryptoCurrencyUIModel, CryptoCurrencyAdapter.CryptoCurrencyViewHolder>(
    object : DiffUtil.ItemCallback<CryptoCurrencyUIModel>() {
        override fun areItemsTheSame(oldItem: CryptoCurrencyUIModel, newItem: CryptoCurrencyUIModel) = oldItem.cryptoCurrency.uuid == newItem.cryptoCurrency.uuid

        override fun areContentsTheSame(oldItem: CryptoCurrencyUIModel, newItem: CryptoCurrencyUIModel) = oldItem == newItem
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CryptoCurrencyViewHolder.create(
        parent = parent,
        onItemClickListener = onItemClickListener,
        onItemLongClickListener = onItemLongClickListener
    )

    override fun onBindViewHolder(holder: CryptoCurrencyViewHolder, position: Int) = holder.bind(getItem(position))

    class CryptoCurrencyViewHolder private constructor(
        private val binding: ItemCryptocurrencyCryptoBinding,
        private val onItemClickListener: OnItemClickListener,
        private val onItemLongClickListener: OnItemLongClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(bindingAdapterPosition)
            }
            itemView.setOnLongClickListener {
                onItemLongClickListener.onItemLongClick(bindingAdapterPosition)
                true
            }
        }

        fun bind(uiModel: CryptoCurrencyUIModel) {
            binding.uiModel = uiModel
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onItemClickListener: OnItemClickListener,
                onItemLongClickListener: OnItemLongClickListener
            ) = CryptoCurrencyViewHolder(
                binding = ItemCryptocurrencyCryptoBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onItemClickListener = onItemClickListener,
                onItemLongClickListener = onItemLongClickListener
            )
        }
    }
}
