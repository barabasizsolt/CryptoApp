package com.example.cryptoapp.wear.screen.main.watchlist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.items
import androidx.wear.compose.material.rememberScalingLazyListState
import com.example.cryptoapp.wear.R
import com.example.cryptoapp.wear.common.Header
import com.example.cryptoapp.wear.common.WearButton
import com.example.cryptoapp.wear.common.convertToPrice

@Composable
fun WatchListScreen(watchListScreenState: WatchListScreenState) {
    when (watchListScreenState.screenState) {
        is WatchListScreenState.ScreenState.Error -> TODO()
        is WatchListScreenState.ScreenState.Loading -> CircularProgressIndicator()
        else -> Unit
    }

    when (watchListScreenState.screenState) {
        is WatchListScreenState.ScreenState.Normal -> ScreenContent(screenState = watchListScreenState)
        else -> Unit
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
private fun ScreenContent(screenState: WatchListScreenState) {
    val scalingLazyListState: ScalingLazyListState = rememberScalingLazyListState()

    Scaffold(
        timeText = { Header(text = "Watch List") },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scalingLazyListState = scalingLazyListState) }
    ) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = scalingLazyListState,
            contentPadding = PaddingValues(top = 20.dp)
        ) {
            item {
                WearButton(
                    painterRes = R.drawable.ic_logout,
                    onClick = screenState::onSignOutClicked
                )
            }
            screenState.watchListSummary?.let {
                if (screenState.cryptoCurrencies.isNotEmpty()) {
                    item {
                        WatchListSummary(watchListSummaryUiModel = it)
                    }
                    items(
                        items = screenState.cryptoCurrencies,
                        key = { it.uuid }
                    ) { item ->
                        CryptoCurrencyItem(
                            iconUrl = item.iconUrl,
                            name = item.name,
                            symbol = item.symbol,
                            price = item.price.convertToPrice(),
                            change = item.change,
                            onClick = { screenState.onItemClicked(id = item.uuid) }
                        )
                    }
                } else {

                }
            }
        }
    }
}