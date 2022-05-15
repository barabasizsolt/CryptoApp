package com.example.cryptoapp.feature.shared.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.cryptoapp.R

@Composable
fun getPrimaryColor() = colorResource(id = R.color.color_primary)

@Composable
fun getBackgroundColor() = if (isSystemInDarkTheme()) Color.Black else Color.White

@Composable
fun getContentColor() = if (isSystemInDarkTheme()) Color.White else Color.Black