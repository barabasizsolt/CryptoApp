package com.example.cryptoapp.wear.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.cryptoapp.auth.useCase.GetCurrentUserUseCase
import com.example.cryptoapp.wear.screen.auth.AuthScreen
import com.example.cryptoapp.wear.screen.auth.AuthScreenState
import com.example.cryptoapp.wear.screen.auth.rememberAuthScreenState
import com.example.cryptoapp.wear.screen.cryptocurrency.watchlist.WatchListScreen
import com.example.cryptoapp.wear.screen.cryptocurrency.watchlist.WatchListScreenState
import com.example.cryptoapp.wear.screen.cryptocurrency.watchlist.rememberWatchListScreenState
import org.koin.androidx.compose.inject

@Composable
fun AppNavigation() {

    val navController = rememberSwipeDismissableNavController()

    SwipeDismissableNavHost(
        navController = navController,
        startDestination = Route.SPLASH,
        modifier = Modifier.background(color = MaterialTheme.colors.background)
    ) {

        composable(route = Route.SPLASH) {
            val getCurrentUser: GetCurrentUserUseCase by inject()
            println("User: ${getCurrentUser()}")
            if (getCurrentUser() == null) navController.navigateToAuth() else navController.navigateToWatchList()
        }

        composable(route = Route.AUTHENTICATION) {
            AuthScreen(authScreenState = rememberAuthScreenState().apply {
                when (action?.consume()) {
                    is AuthScreenState.Action.NavigateToHome -> navController.navigateToMarket()
                    else -> Unit
                }
            })
        }

        composable(route = Route.MARKET) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(text = "Helloooo", modifier = Modifier.align(Alignment.Center))
            }
        }

        composable(route = Route.WATCHLIST) {
            WatchListScreen(watchListScreenState = rememberWatchListScreenState().apply {
                when (action?.consume()) {
                    is WatchListScreenState.Action.OnItemClicked -> {
                        //TODO: somehow open the fcking app
                    }
                    else -> Unit
                }
            })
        }
    }
}

fun NavHostController.navigateToAuth() {
    navigate(route = Route.AUTHENTICATION)
}

fun NavHostController.navigateToMarket() {
    backQueue.clear()
    navigate(route = Route.MARKET)
}

fun NavHostController.navigateToWatchList() {
    navigate(route = Route.WATCHLIST)
}