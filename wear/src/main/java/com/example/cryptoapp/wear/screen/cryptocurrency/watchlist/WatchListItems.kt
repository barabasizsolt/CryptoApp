package com.example.cryptoapp.wear.screen.cryptocurrency.watchlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.AppCard
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TitleCard
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import com.example.cryptoapp.wear.R
import com.example.cryptoapp.wear.common.getFormattedHour
import com.example.cryptoapp.wear.common.isSvg

@Composable
fun WatchListSummary(
    modifier: Modifier = Modifier,
    watchListSummaryUiModel: WatchListSummaryUiModel,
) {
    TitleCard(
        onClick = {}, 
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = watchListSummaryUiModel.totalPrice,
                    style = MaterialTheme.typography.title1
                )
                Text(
                    text = "ACC/USD - AVG - ${System.currentTimeMillis().getFormattedHour()}",
                    style = MaterialTheme.typography.caption3
                )
            }

        },
        contentColor = MaterialTheme.colors.onSurface,
        titleColor = MaterialTheme.colors.onSurface,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = if (watchListSummaryUiModel.totalChange.toDouble() < 0.00) Color.Red else Color.Green,
                shape = MaterialTheme.shapes.large
            )
    ) {
        WatchListItem(
            title = "Total coins:",
            value = watchListSummaryUiModel.totalCoin
        )
        WatchListItem(
            title = "Accumulated vol change:",
            value = if (watchListSummaryUiModel.totalChange.toDouble() < 0.00) "${watchListSummaryUiModel.totalChange}%" else "+${watchListSummaryUiModel.totalChange}%",
            valueColor = if (watchListSummaryUiModel.totalChange.toDouble() < 0.00) Color.Red else Color.Green
        )
        WatchListItem(
            title = "Accumulated volume:",
            value = watchListSummaryUiModel.totalVolume
        )
        WatchListItem(
            title = "Accumulated market cap:",
            value = watchListSummaryUiModel.totalMarketCap
        )
    }
}

@Composable
private fun WatchListItem(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    valueColor: Color = MaterialTheme.colors.onSurface
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.caption2)
        Text(text = value, style = MaterialTheme.typography.caption2, fontWeight = FontWeight.Normal, color = valueColor)
    }
}

@Composable
fun CryptoCurrencyItem(
    modifier: Modifier = Modifier,
    iconUrl: String,
    name: String,
    symbol: String,
    price: String,
    change: String
) {
    Chip(
        label = {
            Text(
                text = price,
                style = MaterialTheme.typography.title2
            )
        },
        secondaryLabel = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "$symbol/$name")
                Text(
                    text = if (change.toDouble() < 0.00) "$change%" else "+$change%",
                    color = if (change.toDouble() < 0.00) Color.Red else Color.Green,
                    style = MaterialTheme.typography.caption3,
                    modifier = Modifier.offset(y = (-10).dp)
                )
            }
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
                contentScale = ContentScale.Fit
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