package com.example.cryptoapp.feature.screen.main.user

import com.example.cryptoapp.feature.shared.utils.ListItem
import com.example.cryptoapp.auth.service.model.UserAvatarType

sealed class ProfileListItem : ListItem {

    data class ErrorState(
        val nothing: Any? = null
    ) : ProfileListItem() {
        override val id = "errorState"
    }

    data class User(
        val userId: String,
        val avatarType: UserAvatarType,
        val email: String,
        val registrationDate: String
    ) : ProfileListItem() {
        override val id = "profile_{$userId}"
    }
}
