package com.example.cryptoapp.wear.screen.main.watchlistDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.AppCard
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.rememberScalingLazyListState
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import com.example.cryptoapp.auth.service.model.formatUserTimeStamp
import com.example.cryptoapp.wear.R
import com.example.cryptoapp.wear.common.Header
import com.example.cryptoapp.wear.common.WearButton
import com.example.cryptoapp.wear.common.getFormattedHour
import com.example.cryptoapp.wear.common.isSvg
import com.example.cryptoapp.wear.screen.main.watchlist.WatchListItem
import com.example.cryptoapp.wear.screen.main.watchlist.WatchListItemSize

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun WatchListDetailScreen(screenState: WatchListDetailScreenState) {
    when (screenState.screenState) {
        is WatchListDetailScreenState.ScreenState.Error -> TODO()
        is WatchListDetailScreenState.ScreenState.Loading -> CircularProgressIndicator()
        else -> Unit
    }

    when (screenState.screenState) {
        is WatchListDetailScreenState.ScreenState.Normal ->ScreenContent(
            screenState = screenState
        )
        else -> Unit
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
private fun ScreenContent(screenState: WatchListDetailScreenState) {
    screenState.cryptoCurrencyDetail?.let {
        val scalingLazyListState: ScalingLazyListState = rememberScalingLazyListState()

        Scaffold(
            vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
            positionIndicator = { PositionIndicator(scalingLazyListState = scalingLazyListState) }
        ) {
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = scalingLazyListState
            ) {
                item {
                    AppCard(
                        modifier = Modifier.padding(all = 8.dp),
                        onClick = { },
                        appName = {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = it.price,
                                    style = MaterialTheme.typography.title3
                                )
                                Text(
                                    text = "${it.symbol}/${it.name} - AVG - ${System.currentTimeMillis().getFormattedHour()}",
                                    style = MaterialTheme.typography.caption3
                                )
                            }
                        },
                        appImage = {
                            Image(
                                painter = rememberImagePainter(
                                    data = it.iconUrl,
                                    builder = { if (it.iconUrl.isSvg()) decoder(SvgDecoder(LocalContext.current)) }
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(size = ChipDefaults.LargeIconSize)
                                    .clip(shape = CircleShape),
                                contentScale = ContentScale.Fit
                            )
                        } ,
                        time = {},
                        title = {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(space = 2.dp)
                            ) {
                                WatchListItem(
                                    title = "24H volume change: ",
                                    value = if (it.change.toDouble() < 0.00) "${it.change}%" else "+${it.change}%",
                                    valueColor = if (it.change.toDouble() < 0.00) Color.Red else Color.Green,
                                    watchListItemSize = WatchListItemSize.NORMAL
                                )
                                WatchListItem(
                                    title = "Volume: ",
                                    value = it.volume,
                                    watchListItemSize = WatchListItemSize.NORMAL
                                )
                                WatchListItem(
                                    title = "Market cap: ",
                                    value = it.marketCap,
                                    watchListItemSize = WatchListItemSize.NORMAL
                                )
                            }
                        }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            WearButton(
                                painterRes = R.drawable.ic_delete,
                                onClick = screenState::onDeleteClicked
                            )
                            WearButton(
                                painterRes = R.drawable.ic_app_open,
                                onClick = screenState::onOpenAppClicked
                            )
                        }
                    }
                }
            }
        }
    }
}