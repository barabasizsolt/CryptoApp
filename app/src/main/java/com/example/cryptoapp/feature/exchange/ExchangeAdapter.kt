package com.example.cryptoapp.feature.exchange

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.databinding.ItemExchangeExchangeBinding
import com.example.cryptoapp.feature.shared.OnItemClickListener
import com.example.cryptoapp.feature.shared.OnItemLongClickListener

class ExchangeAdapter(
    private val onItemClickListener: OnItemClickListener,
    private val onItemLongClickListener: OnItemLongClickListener
) : ListAdapter<ExchangeUIModel, ExchangeAdapter.ExchangeViewHolder>(
    object : DiffUtil.ItemCallback<ExchangeUIModel>() {
        override fun areItemsTheSame(oldItem: ExchangeUIModel, newItem: ExchangeUIModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ExchangeUIModel, newItem: ExchangeUIModel) = oldItem == newItem
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeViewHolder = ExchangeViewHolder.create(
        parent = parent,
        onItemClickListener = onItemClickListener,
        onItemLongClickListener = onItemLongClickListener
    )

    override fun onBindViewHolder(holder: ExchangeViewHolder, position: Int) = holder.bind(getItem(position))

    class ExchangeViewHolder private constructor(
        private val binding: ItemExchangeExchangeBinding,
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

        fun bind(uiModel: ExchangeUIModel) {
            binding.uiModel = uiModel
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onItemClickListener: OnItemClickListener,
                onItemLongClickListener: OnItemLongClickListener
            ) = ExchangeViewHolder(
                binding = ItemExchangeExchangeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onItemClickListener = onItemClickListener,
                onItemLongClickListener = onItemLongClickListener
            )
        }
    }
}
