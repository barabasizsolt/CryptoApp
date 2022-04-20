package com.example.cryptoapp.feature.shared.catalog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean
) {
    Box(modifier = modifier.fillMaxSize()) {
        SwipeRefreshIndicator(
            state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
            refreshTriggerDistance = 80.dp,
            modifier = Modifier.align(alignment = Alignment.TopCenter)
        )
    }
}