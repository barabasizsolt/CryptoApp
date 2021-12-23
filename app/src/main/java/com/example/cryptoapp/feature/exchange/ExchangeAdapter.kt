package com.example.cryptoapp.feature.exchange

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.ItemExchangeExchangeBinding
import com.example.cryptoapp.databinding.ItemExchangeLoadMoreBinding

class ExchangeAdapter(
    private val onExchangeItemClick: (String) -> Unit,
    private val onLoadMoreExchanges: () -> Unit
) : ListAdapter<ExchangeListItem, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<ExchangeListItem>() {
        override fun areItemsTheSame(oldItem: ExchangeListItem, newItem: ExchangeListItem) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ExchangeListItem, newItem: ExchangeListItem) = oldItem == newItem
    }
) {
    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is ExchangeListItem.Exchange -> R.layout.item_exchange_exchange
        is ExchangeListItem.LoadMore -> R.layout.item_exchange_load_more
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.item_exchange_exchange -> ExchangeViewHolder.create(
            parent = parent,
            onExchangeItemClick = onExchangeItemClick
        )
        R.layout.item_exchange_load_more -> LoadMoreViewHolder.create(
            parent = parent
        )
        else -> throw IllegalStateException("Invalid view type: $viewType.")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (val uiModel = getItem(position)) {
        is ExchangeListItem.Exchange -> (holder as ExchangeViewHolder).bind(uiModel)
        is ExchangeListItem.LoadMore -> (holder as LoadMoreViewHolder).bind(uiModel).also {
            onLoadMoreExchanges()
        }
    }

    class LoadMoreViewHolder private constructor(
        private val binding: ItemExchangeLoadMoreBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listItem: ExchangeListItem.LoadMore) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(parent: ViewGroup) = LoadMoreViewHolder(
                binding = ItemExchangeLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    class ExchangeViewHolder private constructor(
        private val binding: ItemExchangeExchangeBinding,
        private val onExchangeItemClick: (String) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onExchangeItemClick(binding.uiModel?.id.orEmpty())
                }
            }
        }

        fun bind(listItem: ExchangeListItem.Exchange) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(parent: ViewGroup, onExchangeItemClick: (String) -> Unit) = ExchangeViewHolder(
                binding = ItemExchangeExchangeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onExchangeItemClick = onExchangeItemClick
            )
        }
    }
}
