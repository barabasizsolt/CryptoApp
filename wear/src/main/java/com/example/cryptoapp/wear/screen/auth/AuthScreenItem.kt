package com.example.cryptoapp.wear.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.cryptoapp.wear.R


@Composable
fun GoogleSignInButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Chip(
        label = {
            Text(
                text = "Continue with Google",
                style = MaterialTheme.typography.caption1
            )
        },
        icon = {
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = null,
                modifier = Modifier.size(size = ChipDefaults.IconSize).wrapContentSize(align = Alignment.Center)
            )
        },
        onClick = onClick,
        colors = ChipDefaults.chipColors(
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onSurface
        ),
        modifier = modifier.fillMaxWidth()
    )
}