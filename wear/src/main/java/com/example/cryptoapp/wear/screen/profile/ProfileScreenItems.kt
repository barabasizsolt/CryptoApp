package com.example.cryptoapp.wear.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.compose.ui.res.painterResource
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import com.example.cryptoapp.wear.common.isSvg

@Composable
fun ProfileItem(
    modifier: Modifier = Modifier,
    title: String,
    iconId: Int
) {
    Chip(
        label = { },
        secondaryLabel = { Text(text = title) },
        icon = {
            Image(
                painter = painterResource(id = iconId),
                contentDescription = null,
                modifier = Modifier
                    .size(size = ChipDefaults.SmallIconSize)
                    .clip(shape = CircleShape),
                contentScale = ContentScale.Fit
            )
        },
        colors = ChipDefaults.chipColors(
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onSurface
        ),
        onClick = { },
        modifier = modifier.fillMaxWidth()
    )
}


