package com.example.cryptoapp.auth.data

import android.net.Uri

data class User(
    val userId: String,
    val avatarType: UserAvatarType,
    val email: String,
    val registrationTimeStamp: Long
)

sealed class UserAvatarType {

    data class UriType(val uri: Uri) : UserAvatarType()

    data class IntType(val id: Int) : UserAvatarType()
}
