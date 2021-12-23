package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.ItemCryptocurrencyCryptoBinding
import com.example.cryptoapp.databinding.ItemCryptocurrencyLoadMoreBinding

class CryptoCurrencyAdapter(
    private val onCryptoCurrencyItemClicked: (String) -> Unit,
    private val onLoadMoreCryptoCurrency: () -> Unit
) : ListAdapter<CryptoCurrencyListItem, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<CryptoCurrencyListItem>() {
        override fun areItemsTheSame(oldItem: CryptoCurrencyListItem, newItem: CryptoCurrencyListItem) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CryptoCurrencyListItem, newItem: CryptoCurrencyListItem) = oldItem == newItem
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.item_cryptocurrency_crypto -> CryptoCurrencyViewHolder.create(
            parent = parent,
            onCryptoCurrencyItemClicked = onCryptoCurrencyItemClicked
        )
        R.layout.item_cryptocurrency_load_more -> LoadMoreViewHolder.create(
            parent = parent
        )
        else -> throw IllegalStateException("Invalid view type: $viewType.")
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is CryptoCurrencyListItem.Crypto -> R.layout.item_cryptocurrency_crypto
        is CryptoCurrencyListItem.LoadMore -> R.layout.item_cryptocurrency_load_more
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (val uiModel = getItem(position)) {
        is CryptoCurrencyListItem.Crypto -> (holder as CryptoCurrencyViewHolder).bind(uiModel)
        is CryptoCurrencyListItem.LoadMore -> (holder as LoadMoreViewHolder).bind(uiModel).also {
            onLoadMoreCryptoCurrency()
        }
    }

    class LoadMoreViewHolder private constructor(
        private val binding: ItemCryptocurrencyLoadMoreBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listItem: CryptoCurrencyListItem.LoadMore) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(parent: ViewGroup) = LoadMoreViewHolder(
                binding = ItemCryptocurrencyLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    class CryptoCurrencyViewHolder private constructor(
        private val binding: ItemCryptocurrencyCryptoBinding,
        private val onCryptoCurrencyItemClicked: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onCryptoCurrencyItemClicked(binding.uiModel?.id.orEmpty())
                }
            }
        }

        fun bind(listItem: CryptoCurrencyListItem.Crypto) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onCryptoCurrencyItemClicked: (String) -> Unit
            ) = CryptoCurrencyViewHolder(
                binding = ItemCryptocurrencyCryptoBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onCryptoCurrencyItemClicked = onCryptoCurrencyItemClicked
            )
        }
    }
}
