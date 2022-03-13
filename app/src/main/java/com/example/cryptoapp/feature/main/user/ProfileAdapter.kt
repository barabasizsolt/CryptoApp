package com.example.cryptoapp.feature.main.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.ItemProfileErrorStateBinding
import com.example.cryptoapp.databinding.ItemProfileProfileBinding
import com.example.cryptoapp.feature.shared.utils.ListItemDiff

class ProfileAdapter(
    private val onChangePasswordClicked: () -> Unit,
    private val onSignOutClicked: () -> Unit,
    private val onTryAgainButtonClicked: () -> Unit
) : ListAdapter<ProfileListItem, RecyclerView.ViewHolder>(ListItemDiff()) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is ProfileListItem.ErrorState -> R.layout.item_profile_error_state
        is ProfileListItem.User -> R.layout.item_profile_profile
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.item_profile_profile -> UserViewHolder.create(
            parent = parent,
            onChangePasswordClicked = onChangePasswordClicked,
            onSignOutClicked = onSignOutClicked
        )
        R.layout.item_profile_error_state -> ErrorStateViewHolder.create(
            parent = parent,
            onTryAgainButtonClicked = onTryAgainButtonClicked
        )
        else -> throw IllegalStateException("Invalid view type: $viewType.")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (val uiModel = getItem(position)) {
        is ProfileListItem.ErrorState -> (holder as ErrorStateViewHolder).bind(uiModel)
        is ProfileListItem.User -> (holder as UserViewHolder).bind(uiModel)
    }

    class ErrorStateViewHolder private constructor(
        private val binding: ItemProfileErrorStateBinding,
        private val onTryAgainButtonClicked: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.errorStateLayout.tryAgain.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onTryAgainButtonClicked()
                }
            }
        }

        fun bind(listItem: ProfileListItem.ErrorState) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(parent: ViewGroup, onTryAgainButtonClicked: () -> Unit) = ErrorStateViewHolder(
                binding = ItemProfileErrorStateBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onTryAgainButtonClicked = onTryAgainButtonClicked
            )
        }
    }

    class UserViewHolder private constructor(
        private val binding: ItemProfileProfileBinding,
        private val onChangePasswordClicked: () -> Unit,
        private val onSignOutClicked: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.signOut.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onSignOutClicked()
                }
            }
            binding.updatePassword.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onChangePasswordClicked()
                }
            }
        }

        fun bind(listItem: ProfileListItem.User) {
            binding.uiModel = listItem
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onChangePasswordClicked: () -> Unit,
                onSignOutClicked: () -> Unit
            ) = UserViewHolder(
                binding = ItemProfileProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onChangePasswordClicked = onChangePasswordClicked,
                onSignOutClicked = onSignOutClicked
            )
        }
    }
}
