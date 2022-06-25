package com.example.cryptoapp.wear.screen.cryptocurrency.watchlist

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.HorizontalPageIndicator
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.InlineSlider
import androidx.wear.compose.material.InlineSliderDefaults
import androidx.wear.compose.material.PageIndicatorState
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.items
import androidx.wear.compose.material.rememberScalingLazyListState
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
        is WatchListScreenState.ScreenState.Normal -> ScreenContent(watchListScreenState = watchListScreenState)
        else -> Unit
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
private fun ScreenContent(watchListScreenState: WatchListScreenState) {
    val scalingLazyListState: ScalingLazyListState = rememberScalingLazyListState()

    watchListScreenState.watchListSummary?.let {
        Scaffold(
            timeText = { Header(text = "Watch List") },
            vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
            positionIndicator = { PositionIndicator(scalingLazyListState = scalingLazyListState) }
        ) {
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = scalingLazyListState,
                contentPadding = PaddingValues(top = 40.dp)
            ) {
                if (watchListScreenState.cryptoCurrencies.isNotEmpty()) {
                    item {
                        WatchListSummary(watchListSummaryUiModel = it)
                    }
                    items(
                        items = watchListScreenState.cryptoCurrencies,
                        key = { it.uuid }
                    ) { item ->
                        CryptoCurrencyItem(
                            iconUrl = item.iconUrl,
                            name = item.name,
                            symbol = item.symbol,
                            price = item.price.convertToPrice(),
                            change = item.change
                        )
                    }
                } else {

                }
                item { WearButton(title = "Profile", onClick = watchListScreenState::onProfileClicked) }
            }
        }
    }
}