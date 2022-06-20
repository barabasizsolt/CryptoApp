package com.example.cryptoapp.wear.screen.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.HorizontalPageIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.rememberScalingLazyListState
import com.example.cryptoapp.wear.common.Header

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun AuthScreen(authScreenState: AuthScreenState) {
    when (authScreenState.screenState) {
        is AuthScreenState.ScreenState.Normal -> ScreenContent(authScreenState = authScreenState)
        else -> Unit
    }

    when (authScreenState.screenState) {
        is AuthScreenState.ScreenState.Error -> {
            //TODO: alert dialog with the error message
        }
        is AuthScreenState.ScreenState.Loading -> CircularProgressIndicator()
        else -> Unit
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
private fun ScreenContent(authScreenState: AuthScreenState) {
    MaterialTheme {
        val scalingLazyListState: ScalingLazyListState = rememberScalingLazyListState()

        val loginWithGoogleAccountLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            if (result.resultCode == Activity.RESULT_OK && data != null) { authScreenState.loginWithGoogle(intent = data) }
        }

        Scaffold(
            timeText = { Header(text = "Welcome Back!") },
            vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
            positionIndicator = { PositionIndicator(scalingLazyListState = scalingLazyListState) }
        ) {
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = scalingLazyListState
            ) {
                item {
                    GoogleSignInButton(
                        onClick = {
                            val intent = authScreenState.getIntentForGoogleLogin()
                            loginWithGoogleAccountLauncher.launch(intent)
                        }
                    )
                }
            }
        }
    }
}