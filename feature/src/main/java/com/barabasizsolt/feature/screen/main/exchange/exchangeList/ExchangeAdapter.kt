package com.barabasizsolt.feature.screen.main.exchange.exchangeList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.barabasizsolt.feature.R
import com.barabasizsolt.feature.databinding.ItemExchangeErrorStateBinding
import com.barabasizsolt.feature.databinding.ItemExchangeExchangeBinding
import com.barabasizsolt.feature.databinding.ItemExchangeLoadMoreBinding
import com.barabasizsolt.feature.shared.utils.ListItemDiff

class ExchangeAdapter(
    private val onExchangeItemClick: (String) -> Unit,
    private val onLoadMoreExchanges: () -> Unit,
    private val onTryAgainButtonClicked: () -> Unit
) : ListAdapter<ExchangeListItem, RecyclerView.ViewHolder>(ListItemDiff()) {
    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is ExchangeListItem.Exchange -> R.layout.item_exchange_exchange
        is ExchangeListItem.LoadMore -> R.layout.item_exchange_load_more
        is ExchangeListItem.ErrorState -> R.layout.item_exchange_error_state
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.item_exchange_exchange -> ExchangeViewHolder.create(
            parent = parent,
            onExchangeItemClick = onExchangeItemClick
        )
        R.layout.item_exchange_load_more -> LoadMoreViewHolder.create(
            parent = parent
        )
        R.layout.item_exchange_error_state -> ErrorStateViewHolder.create(
            parent = parent,
            onTryAgainButtonClicked = onTryAgainButtonClicked
        )
        else -> throw IllegalStateException("Invalid view type: $viewType.")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (val uiModel = getItem(position)) {
        is ExchangeListItem.Exchange -> (holder as ExchangeViewHolder).bind(uiModel)
        is ExchangeListItem.LoadMore -> (holder as LoadMoreViewHolder).bind(uiModel).also {
            onLoadMoreExchanges()
        }
        is ExchangeListItem.ErrorState -> (holder as ErrorStateViewHolder).bind(uiModel)
    }

    class ErrorStateViewHolder private constructor(
        private val binding: ItemExchangeErrorStateBinding,
        private val onTryAgainButtonClicked: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.errorStateLayout.tryAgain.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onTryAgainButtonClicked()
                }
            }
        }

        fun bind(listItem: ExchangeListItem.ErrorState) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(parent: ViewGroup, onTryAgainButtonClicked: () -> Unit) = ErrorStateViewHolder(
                binding = ItemExchangeErrorStateBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onTryAgainButtonClicked = onTryAgainButtonClicked
            )
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
