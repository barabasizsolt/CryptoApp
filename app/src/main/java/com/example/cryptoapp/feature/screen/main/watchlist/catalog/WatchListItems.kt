package com.example.cryptoapp.feature.screen.main.watchlist.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import com.example.cryptoapp.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import com.example.cryptoapp.feature.shared.utils.isSvg

@Composable
fun WatchListPlaceHolder(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(all = dimensionResource(id = R.dimen.content_padding))
                .align(alignment = Alignment.TopCenter),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = dimensionResource(id = R.dimen.error_state_margin) * 2),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.empty_watchlist),
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.screen_padding)))
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    Text(
                        text = stringResource(id = R.string.add_to_watchlist).toUpperCase(locale = Locale.current),
                        style = MaterialTheme.typography.button,
                        color = Color.Black,
                        modifier = Modifier.padding(all = dimensionResource(id = R.dimen.content_padding))
                    )
                }
            }
        }
    }
}

@Composable
fun DeleteItem(
    modifier: Modifier = Modifier,
    color: Color
) {
    Card(
        modifier = modifier
            .fillMaxSize(),
        backgroundColor = color,
        shape = MaterialTheme.shapes.medium
    ) {
        Box(modifier = Modifier.padding(all = dimensionResource(id = R.dimen.content_padding))) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.align(alignment = Alignment.CenterEnd)
            )
        }
    }
}

@Composable
fun CryptoCurrencyItem(
    modifier: Modifier = Modifier,
    iconUrl: String,
    name: String,
    symbol: String,
    price: String,
    change: String,
    volume: String,
    marketCap: String
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(height = dimensionResource(id = R.dimen.recycler_view_card_height)),
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface
    ) {
        Box {
            Row(
                modifier = Modifier
                    .padding(all = dimensionResource(id = R.dimen.content_padding))
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CryptoCurrencyLogo(
                    iconUrl = iconUrl,
                    name = name,
                    symbol = symbol
                )
                CryptoCurrencyInfo(
                    change = change,
                    volume = volume,
                    marketCap = marketCap
                )
            }
            CryptoCurrencyPrice(
                price = price,
                modifier = Modifier.align(alignment = Alignment.Center)
            )
        }
    }
}

@Composable
private fun CryptoCurrencyLogo(
    modifier: Modifier = Modifier,
    iconUrl: String,
    name: String,
    symbol: String
) {
    Column(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.small_padding))) {
            Image(
                painter = rememberImagePainter(
                    data = iconUrl,
                    builder = { if (iconUrl.isSvg()) { decoder(SvgDecoder(LocalContext.current)) } }
                ),
                contentDescription = null,
                modifier = Modifier.size(size = dimensionResource(id = R.dimen.recycler_view_card_logo_size))
            )
            Text(
                text = symbol,
                style = MaterialTheme.typography.caption
            )
        }
        Text(
            text = name,
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CryptoCurrencyPrice(
    modifier: Modifier = Modifier,
    price: String
) {
    Text(
        text = price,
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.content_padding)),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun CryptoCurrencyInfo(
    modifier: Modifier = Modifier,
    change: String,
    volume: String,
    marketCap: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.small_padding)),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = if (change.toDouble() < 0.00) change else "+$change",
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.End,
                color = if (change.toDouble() < 0.00) Color.Red else Color.Green
            )
            Text(
                text = volume,
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.End
            )
            Text(
                text = marketCap,
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.End
            )
        }
        Column {
            Text(
                text = stringResource(id = R.string.daily_change),
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(id = R.string.volume),
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(id = R.string.market_cap),
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.Bold
            )
        }
    }
}