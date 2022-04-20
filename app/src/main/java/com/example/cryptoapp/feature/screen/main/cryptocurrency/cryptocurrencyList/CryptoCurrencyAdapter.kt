package com.example.cryptoapp.feature.screen.main.cryptocurrency.cryptocurrencyList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.ItemCryptocurrencyCryptoBinding
import com.example.cryptoapp.databinding.ItemCryptocurrencyErrorStateBinding
import com.example.cryptoapp.databinding.ItemCryptocurrencyLoadMoreBinding
import com.example.cryptoapp.feature.shared.utils.ListItemDiff

class CryptoCurrencyAdapter(
    private val onCryptoCurrencyItemClicked: (String) -> Unit,
    private val onLoadMoreCryptoCurrency: () -> Unit,
    private val onTryAgainButtonClicked: () -> Unit
) : ListAdapter<CryptoCurrencyListItem, RecyclerView.ViewHolder>(ListItemDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.item_cryptocurrency_crypto -> CryptoCurrencyViewHolder.create(
            parent = parent,
            onCryptoCurrencyItemClicked = onCryptoCurrencyItemClicked
        )
        R.layout.item_cryptocurrency_load_more -> LoadMoreViewHolder.create(
            parent = parent
        )
        R.layout.item_cryptocurrency_error_state -> ErrorStateViewHolder.create(
            parent = parent,
            onTryAgainButtonClicked = onTryAgainButtonClicked
        )
        else -> throw IllegalStateException("Invalid view type: $viewType.")
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is CryptoCurrencyListItem.Crypto -> R.layout.item_cryptocurrency_crypto
        is CryptoCurrencyListItem.LoadMore -> R.layout.item_cryptocurrency_load_more
        is CryptoCurrencyListItem.ErrorState -> R.layout.item_cryptocurrency_error_state
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (val uiModel = getItem(position)) {
        is CryptoCurrencyListItem.Crypto -> (holder as CryptoCurrencyViewHolder).bind(uiModel)
        is CryptoCurrencyListItem.LoadMore -> (holder as LoadMoreViewHolder).bind(uiModel).also {
            onLoadMoreCryptoCurrency()
        }
        is CryptoCurrencyListItem.ErrorState -> (holder as ErrorStateViewHolder).bind(uiModel)
    }

    class ErrorStateViewHolder private constructor(
        private val binding: ItemCryptocurrencyErrorStateBinding,
        private val onTryAgainButtonClicked: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.errorStateLayout.tryAgain.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onTryAgainButtonClicked()
                }
            }
        }

        fun bind(listItem: CryptoCurrencyListItem.ErrorState) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(parent: ViewGroup, onTryAgainButtonClicked: () -> Unit) = ErrorStateViewHolder(
                binding = ItemCryptocurrencyErrorStateBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onTryAgainButtonClicked = onTryAgainButtonClicked
            )
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
                    onCryptoCurrencyItemClicked(binding.uiModel?.cryptoCurrencyId.orEmpty())
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
