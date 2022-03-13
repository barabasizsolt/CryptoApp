package com.example.cryptoapp.feature.main.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.ItemCategoryCategoryBinding
import com.example.cryptoapp.databinding.ItemCategoryErrorStateBinding
import com.example.cryptoapp.feature.shared.utils.ListItemDiff

class CategoryAdapter(
    private val onCategoryItemClick: (String) -> Unit,
    private val onTryAgainButtonClicked: () -> Unit
) : ListAdapter<CategoryListItem, RecyclerView.ViewHolder>(ListItemDiff()) {
    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is CategoryListItem.Category -> R.layout.item_category_category
        is CategoryListItem.ErrorState -> R.layout.item_category_error_state
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.item_category_category -> CategoryViewHolder.create(
            parent = parent,
            onCategoryItemClick = onCategoryItemClick
        )
        R.layout.item_category_error_state -> ErrorStateViewHolder.create(
            parent = parent,
            onTryAgainButtonClicked = onTryAgainButtonClicked
        )
        else -> throw IllegalStateException("Invalid view type: $viewType.")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (val uiModel = getItem(position)) {
        is CategoryListItem.Category -> (holder as CategoryViewHolder).bind(uiModel)
        is CategoryListItem.ErrorState -> (holder as ErrorStateViewHolder).bind(uiModel)
    }

    class ErrorStateViewHolder private constructor(
        private val binding: ItemCategoryErrorStateBinding,
        private val onTryAgainButtonClicked: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.errorStateLayout.tryAgain.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onTryAgainButtonClicked()
                }
            }
        }

        fun bind(listItem: CategoryListItem.ErrorState) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(parent: ViewGroup, onTryAgainButtonClicked: () -> Unit) = ErrorStateViewHolder(
                binding = ItemCategoryErrorStateBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onTryAgainButtonClicked = onTryAgainButtonClicked
            )
        }
    }

    class CategoryViewHolder private constructor(
        private val binding: ItemCategoryCategoryBinding,
        private val onCategoryItemClick: (String) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onCategoryItemClick(binding.uiModel?.id.orEmpty())
                }
            }
        }

        fun bind(listItem: CategoryListItem.Category) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(parent: ViewGroup, onCategoryItemClick: (String) -> Unit) = CategoryViewHolder(
                binding = ItemCategoryCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onCategoryItemClick = onCategoryItemClick
            )
        }
    }
}
