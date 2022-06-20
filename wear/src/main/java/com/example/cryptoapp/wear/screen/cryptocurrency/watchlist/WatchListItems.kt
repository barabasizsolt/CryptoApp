package com.example.cryptoapp.wear.screen.cryptocurrency.watchlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import com.example.cryptoapp.wear.R
import com.example.cryptoapp.wear.common.isSvg

@Composable
fun CryptoCurrencyItem(
    modifier: Modifier = Modifier,
    iconUrl: String,
    name: String,
    price: String,
    change: String
) {
    Chip(
        label = { Text(text = name) },
        secondaryLabel = { Text(text = price) },
        icon = {
            Image(
                painter = rememberImagePainter(
                    data = iconUrl,
                    builder = { if (iconUrl.isSvg()) decoder(SvgDecoder(LocalContext.current)) }
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(size = ChipDefaults.LargeIconSize)
                    .clip(shape = CircleShape),
                contentScale = ContentScale.Fit
            )
        },
        onClick = {  },
        colors = ChipDefaults.chipColors(
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onSurface
        ),
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 0.5.dp,
                color = if (change.toDouble() < 0.00) Color.Red else Color.Green,
                shape = CircleShape
            )
    )
}