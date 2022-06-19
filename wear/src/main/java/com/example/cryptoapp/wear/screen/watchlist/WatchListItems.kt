package com.example.cryptoapp.wear.screen.watchlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import com.example.cryptoapp.wear.isSvg

@Composable
private fun CryptoCurrencyItem(
    modifier: Modifier = Modifier,
    iconUrl: String,
    name: String,
    price: String,
    change: String
) {
    Chip(
        label = { Text(text = name) },
        secondaryLabel = {
            Text(text = price)
            Spacer(modifier = modifier.weight(weight = 1f))
            Text(text = change, modifier = Modifier.offset(y = (-10).dp))
        },
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
                contentScale = ContentScale.Crop
            )
        },
        onClick = {  },
        colors = ChipDefaults.chipColors(
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onSurface
        ),
        modifier = modifier.fillMaxWidth()
    )
}