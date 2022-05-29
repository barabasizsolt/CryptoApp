package com.example.cryptoapp.wear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipColors
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.contentColorFor
import androidx.wear.compose.material.rememberScalingLazyListState
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder

class WearActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                val scalingLazyListState: ScalingLazyListState = rememberScalingLazyListState()

                Scaffold(
                    timeText = {
                        TimeText()
                    },
                    vignette = {
                        Vignette(vignettePosition = VignettePosition.TopAndBottom)
                    },
                    positionIndicator = {
                        PositionIndicator(
                            scalingLazyListState = scalingLazyListState
                        )
                    }
                ) {
                    ScalingLazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = scalingLazyListState
                    ) {
                        items(count = 10) {
                            CryptoCurrencyItem()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CryptoCurrencyItem(modifier: Modifier = Modifier) {
    Chip(
        label = {
            Text(
                text = "Bitcoin",
                //style = MaterialTheme.typography.title3
            )
        },
        secondaryLabel = {
            Text(
                text = "$29,1732.12",
                //style = MaterialTheme.typography.body1
            )
        },
        icon = {
            Image(
                painter = rememberImagePainter(
                    data = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    builder = {
                        decoder(SvgDecoder(LocalContext.current))
                    }
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