package com.example.cryptoapp.wear.screen.profile

import android.net.Uri
import android.os.Parcelable
import com.example.cryptoapp.auth.service.model.User
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserUiModel(
    val userId: String,
    val photo: Uri?,
    val email: String,
    val registrationDate: String,
    val lastSignInDate: String,
    val userName: String,
    val phoneNumber: String,
    val isAnonymous: String,
) : Parcelable

fun User.toUiModel(): UserUiModel = UserUiModel(
    userId = userId,
    photo = photo,
    email = email,
    registrationDate = registrationDate,
    lastSignInDate = lastSignInDate,
    userName = userName,
    phoneNumber = phoneNumber,
    isAnonymous = isAnonymous
)