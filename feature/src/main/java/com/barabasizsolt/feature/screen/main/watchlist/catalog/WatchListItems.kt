package com.barabasizsolt.feature.screen.main.watchlist.catalog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import com.barabasizsolt.feature.R
import com.barabasizsolt.feature.shared.catalog.CryptoAppButton
import com.barabasizsolt.feature.shared.utils.getFormattedHour
import com.barabasizsolt.feature.shared.utils.isSvg

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
            shape = MaterialTheme.shapes.medium,
            elevation = if (isSystemInDarkTheme()) dimensionResource(id = R.dimen.dark_mode_card_elevation) else dimensionResource(id = R.dimen.zero_elevation)
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
fun WatchListSummary(
    modifier: Modifier = Modifier,
    totalPrice: String,
    iconUrl: String,
    totalCoin: String,
    totalChange: String,
    totalVolume: String,
    totalMarketCap: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(
            width = 1.dp,
            color = if (totalChange.toDouble() < 0.00) Color.Red else Color.Green
        ),
       backgroundColor = MaterialTheme.colors.surface,
       elevation = if (isSystemInDarkTheme()) dimensionResource(id = R.dimen.dark_mode_card_elevation) else dimensionResource(id = R.dimen.zero_elevation)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimensionResource(id = R.dimen.screen_padding)),
            verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.content_padding) * 2)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.small_padding))) {
                    Text(
                        text = totalPrice,
                        style = MaterialTheme.typography.h3,
                        color = MaterialTheme.colors.onSurface,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "PRICE/USD/24H - AVG - ${System.currentTimeMillis().getFormattedHour()}",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Image(
                    painter = rememberImagePainter(
                        data = iconUrl,
                        builder = { if (iconUrl.isSvg()) { decoder(SvgDecoder(LocalContext.current)) } }
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(size = dimensionResource(id = R.dimen.recycler_view_card_logo_size))
                )
            }
            Divider()
            WatchListSummaryInfoHolder(
                totalCoin = totalCoin,
                totalChange = totalChange,
                totalVolume = totalVolume,
                totalMarketCap = totalMarketCap
            )
            Divider()
            CryptoAppButton(
                text = stringResource(id = R.string.add_to_watchlist),
                enabled = true,
                isLoading = false,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun WatchListSummaryInfoHolder(
    modifier: Modifier = Modifier,
    totalCoin: String,
    totalChange: String,
    totalVolume: String,
    totalMarketCap: String
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.small_padding))
    ) {
        WatchListSummaryInfo(text = stringResource(id = R.string.total_coin), value = totalCoin)
        WatchListSummaryInfo(text = stringResource(id = R.string.total_change), value = totalChange, isChange = true)
        WatchListSummaryInfo(text = stringResource(id = R.string.total_volume), value = totalVolume)
        WatchListSummaryInfo(text = stringResource(id = R.string.total_market_cap), value = totalMarketCap)
    }
}

@Composable
private fun WatchListSummaryInfo(
    modifier: Modifier = Modifier,
    text: String,
    value: String,
    isChange: Boolean = false
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Bold
        )
        if (isChange) {
            Text(
                text = if (value.toDouble() < 0.00) "$value%" else "+$value%",
                style = MaterialTheme.typography.body2,
                color = if (value.toDouble() < 0.00) Color.Red else Color.Green
            )
        } else {
            Text(
                text = value,
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CryptoCurrencyItem(
    modifier: Modifier = Modifier,
    iconUrl: String,
    name: String,
    symbol: String,
    price: String,
    change: String,
    volume: String,
    marketCap: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(height = dimensionResource(id = R.dimen.recycler_view_card_height)),
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface,
        elevation = if (isSystemInDarkTheme()) dimensionResource(id = R.dimen.dark_mode_card_elevation) else dimensionResource(id = R.dimen.zero_elevation),
        onClick = onClick
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
    marketCap: String,
    showLargeText: Boolean = false
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.small_padding)),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = if (change.toDouble() < 0.00) "$change%" else "+$change%",
                style = if (showLargeText) MaterialTheme.typography.body2 else MaterialTheme.typography.caption,
                textAlign = TextAlign.End,
                color = if (change.toDouble() < 0.00) Color.Red else Color.Green
            )
            Text(
                text = volume,
                style = if (showLargeText) MaterialTheme.typography.body2 else MaterialTheme.typography.caption,
                textAlign = TextAlign.End
            )
            Text(
                text = marketCap,
                style = if (showLargeText) MaterialTheme.typography.body2 else MaterialTheme.typography.caption,
                textAlign = TextAlign.End
            )
        }
        Column {
            Text(
                text = stringResource(id = R.string.daily_change),
                style = if (showLargeText) MaterialTheme.typography.body2 else MaterialTheme.typography.caption,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(id = R.string.volume),
                style = if (showLargeText) MaterialTheme.typography.body2 else MaterialTheme.typography.caption,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(id = R.string.market_cap),
                style = if (showLargeText) MaterialTheme.typography.body2 else MaterialTheme.typography.caption,
                fontWeight = FontWeight.Bold
            )
        }
    }
}