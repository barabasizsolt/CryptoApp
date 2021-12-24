package com.example.cryptoapp.feature.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.ItemNewsErrorStateBinding
import com.example.cryptoapp.databinding.ItemNewsLoadMoreBinding
import com.example.cryptoapp.databinding.ItemNewsNewsBinding
import com.example.cryptoapp.feature.shared.ListItemDiff

class NewsAdapter(
    private val onNewsItemClicked: (String) -> Unit,
    private val onLoadMoreBound: () -> Unit,
    private val onTryAgainButtonClicked: () -> Unit
) : ListAdapter<NewsListItem, RecyclerView.ViewHolder>(ListItemDiff()) {
    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is NewsListItem.LoadMore -> R.layout.item_news_load_more
        is NewsListItem.News -> R.layout.item_news_news
        is NewsListItem.ErrorState -> R.layout.item_news_error_state
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.item_news_load_more -> LoadMoreViewHolder.create(
            parent = parent
        )
        R.layout.item_news_news -> NewsViewHolder.create(
            parent = parent,
            onNewsItemClicked = onNewsItemClicked,
        )
        R.layout.item_news_error_state -> ErrorStateViewHolder.create(
            parent = parent,
            onTryAgainButtonClicked = onTryAgainButtonClicked
        )
        else -> throw IllegalStateException("Invalid view type: $viewType.")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (val uiModel = getItem(position)) {
        is NewsListItem.LoadMore -> (holder as LoadMoreViewHolder).bind(uiModel).also {
            onLoadMoreBound()
        }
        is NewsListItem.News -> (holder as NewsViewHolder).bind(uiModel)
        is NewsListItem.ErrorState -> (holder as ErrorStateViewHolder).bind(uiModel)
    }

    class ErrorStateViewHolder private constructor(
        private val binding: ItemNewsErrorStateBinding,
        private val onTryAgainButtonClicked: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.errorStateLayout.tryAgain.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onTryAgainButtonClicked()
                }
            }
        }

        fun bind(listItem: NewsListItem.ErrorState) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(parent: ViewGroup, onTryAgainButtonClicked: () -> Unit) = ErrorStateViewHolder(
                binding = ItemNewsErrorStateBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onTryAgainButtonClicked = onTryAgainButtonClicked
            )
        }
    }

    class LoadMoreViewHolder private constructor(
        private val binding: ItemNewsLoadMoreBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listItem: NewsListItem.LoadMore) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(parent: ViewGroup) = LoadMoreViewHolder(
                binding = ItemNewsLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    class NewsViewHolder private constructor(
        private val binding: ItemNewsNewsBinding,
        private val onNewsItemClicked: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onNewsItemClicked(binding.uiModel?.url.orEmpty())
                }
            }
        }

        fun bind(listItem: NewsListItem.News) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(parent: ViewGroup, onNewsItemClicked: (String) -> Unit) = NewsViewHolder(
                binding = ItemNewsNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onNewsItemClicked = onNewsItemClicked
            )
        }
    }
}
