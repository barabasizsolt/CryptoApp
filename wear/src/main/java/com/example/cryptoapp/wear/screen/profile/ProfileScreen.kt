package com.example.cryptoapp.wear.screen.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.rememberScalingLazyListState
import com.example.cryptoapp.wear.R
import com.example.cryptoapp.wear.common.Header
import com.example.cryptoapp.wear.common.WearButton

@Composable
fun ProfileScreen(screenState: ProfileScreenState) {

    ScreenContent(screenState = screenState)

    when (screenState.screenState) {
        is ProfileScreenState.ScreenState.Error -> TODO()
        is ProfileScreenState.ScreenState.Loading -> CircularProgressIndicator()
        else -> Unit
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun ScreenContent(screenState: ProfileScreenState) {
    screenState.user?.let {
        val scalingLazyListState: ScalingLazyListState = rememberScalingLazyListState()

        Scaffold(
            timeText = { Header(text = "Profile") },
            vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
            positionIndicator = { PositionIndicator(scalingLazyListState = scalingLazyListState) }
        ) {
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = scalingLazyListState
            ) {
                item { ProfileItem(title = it.userName, iconId = R.drawable.ic_user) }
                item { ProfileItem(title = it.email, iconId = R.drawable.ic_email) }
                item { ProfileItem(title = it.registrationDate, iconId = R.drawable.ic_date) }
                item { WearButton(title = "Sign Out", onClick = screenState::onSignOutClicked) }
            }
        }
    }
}