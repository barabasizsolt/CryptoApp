package com.example.cryptoapp.feature.screen.main.watchlist.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import com.example.cryptoapp.data.model.cryptocurrency.CryptoCurrency
import com.example.cryptoapp.feature.shared.utils.isSvg

@Composable
fun CryptoCurrencyItem(
    modifier: Modifier = Modifier,
    iconUrl: String,
    name: String,
    symbol: String,
    price: String,
    change: String
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface
    ) {
        Row(
            modifier = Modifier.padding(all = 8.dp),
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
            CryptoCurrencyInfo(change = change)
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
        horizontalArrangement = Arrangement.spacedBy(space = 4.dp)
    ) {
        Column {
            Image(
                painter = rememberImagePainter(
                    data = iconUrl,
                    builder = { if (iconUrl.isSvg()) { decoder(SvgDecoder(LocalContext.current)) } }
                ),
                contentDescription = null,
                modifier = Modifier.size(size = 50.dp)
            )
            Text(
                text = name,
                style = MaterialTheme.typography.caption
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
        modifier = modifier.padding(horizontal = 8.dp),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun CryptoCurrencyInfo(
    modifier: Modifier = Modifier,
    change: String,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = change,
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.End
        )
        Text(
            text = "24H",
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.Bold
        )
    }
}