package com.example.cryptoapp.feature.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.databinding.ItemNewsNewsBinding
import com.example.cryptoapp.feature.shared.OnItemClickListener
import com.example.cryptoapp.feature.shared.OnItemLongClickListener

class NewsAdapter(
    private val onItemClickListener: OnItemClickListener,
    private val onItemLongClickListener: OnItemLongClickListener
) :
    ListAdapter<NewsUIModel, NewsAdapter.NewsViewHolder>(
        object : DiffUtil.ItemCallback<NewsUIModel>() {
            override fun areItemsTheSame(oldItem: NewsUIModel, newItem: NewsUIModel) =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: NewsUIModel, newItem: NewsUIModel) = oldItem == newItem
        }
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder = NewsViewHolder.create(
        parent = parent,
        onItemClickListener = onItemClickListener,
        onItemLongClickListener = onItemLongClickListener
    )

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) = holder.bind(getItem(position))

    class NewsViewHolder private constructor(
        private val binding: ItemNewsNewsBinding,
        private val onItemClickListener: OnItemClickListener,
        private val onItemLongClickListener: OnItemLongClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(bindingAdapterPosition)
            }
            itemView.setOnLongClickListener {
                onItemLongClickListener.onItemLongClick(bindingAdapterPosition)
                true
            }
        }

        fun bind(uiModel: NewsUIModel) {
            binding.uiModel = uiModel
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onItemClickListener: OnItemClickListener,
                onItemLongClickListener: OnItemLongClickListener
            ) = NewsAdapter.NewsViewHolder(
                binding = ItemNewsNewsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onItemClickListener = onItemClickListener,
                onItemLongClickListener = onItemLongClickListener
            )
        }
    }
}
