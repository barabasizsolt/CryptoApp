package com.example.cryptoapp.auth.service.model

import android.icu.text.SimpleDateFormat
import android.net.Uri
import java.util.*

data class User(
    val userId: String,
    val photoUrl: Uri?,
    val email: String,
    val registrationDate: String,
    val lastSignInDate: String,
    val userName: String,
    val phoneNumber: String,
    val isAnonymous: String,
)

fun Long.formatUserTimeStamp(): String = SimpleDateFormat("MMM dd, yyy", Locale.getDefault()).format(this)
