package com.example.cryptoapp.feature.screen.main.watchlist.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import com.example.cryptoapp.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import com.example.cryptoapp.data.model.cryptocurrency.CryptoCurrency
import com.example.cryptoapp.feature.shared.utils.isSvg

@Composable
fun WatchListPlaceHolder(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(id = R.dimen.content_padding)),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimensionResource(id = R.dimen.error_state_margin)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.empty_watchlist),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.screen_padding) * 2))
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
        Row(
            modifier = Modifier.padding(all = dimensionResource(id = R.dimen.content_padding)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CryptoCurrencyLogo(
                iconUrl = iconUrl,
                name = name,
                symbol = symbol
            )
            CryptoCurrencyPrice(
                price = price,
                modifier = Modifier.weight(weight = 1f)
            )
            CryptoCurrencyInfo(
                change = change,
                volume = volume,
                marketCap = marketCap
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
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.small_padding))
    ) {
        Column {
            Image(
                painter = rememberImagePainter(
                    data = iconUrl,
                    builder = { if (iconUrl.isSvg()) { decoder(SvgDecoder(LocalContext.current)) } }
                ),
                contentDescription = null,
                modifier = Modifier.size(size = dimensionResource(id = R.dimen.recycler_view_card_logo_size))
            )
            Text(
                text = name,
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = symbol,
            style = MaterialTheme.typography.caption
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
                text = change,
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.End
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