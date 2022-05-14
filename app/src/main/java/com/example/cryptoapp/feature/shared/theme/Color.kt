package com.example.cryptoapp.feature.shared.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.cryptoapp.R

@Composable
fun getPrimaryColor() = colorResource(id = R.color.orange)

@Composable
fun getBackgroundColor() = if (isSystemInDarkTheme()) colorResource(id = R.color.dark_mode_background_color) else Color.White

@Composable
fun getContentColor() = if (isSystemInDarkTheme()) colorResource(id = R.color.dark_mode_text_color) else Color.Black