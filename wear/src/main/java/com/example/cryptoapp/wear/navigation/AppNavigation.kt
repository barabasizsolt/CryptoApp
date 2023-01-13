package com.example.cryptoapp.wear.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.cryptoapp.auth.useCase.GetCurrentUserUseCase
import com.example.cryptoapp.wear.screen.auth.AuthScreen
import com.example.cryptoapp.wear.screen.auth.AuthScreenState
import com.example.cryptoapp.wear.screen.auth.rememberAuthScreenState
import com.example.cryptoapp.wear.screen.main.watchlist.WatchListScreen
import com.example.cryptoapp.wear.screen.main.watchlist.WatchListScreenState
import com.example.cryptoapp.wear.screen.main.watchlist.rememberWatchListScreenState
import com.example.cryptoapp.wear.screen.main.watchlistDetail.WatchListDetailScreen
import com.example.cryptoapp.wear.screen.main.watchlistDetail.WatchListDetailScreenState
import com.example.cryptoapp.wear.screen.main.watchlistDetail.rememberWatchListDetailScreenState
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
            if (getCurrentUser() == null) navController.navigateToAuth() else navController.navigateToWatchList()
        }

        composable(route = Route.AUTHENTICATION) {
            AuthScreen(authScreenState = rememberAuthScreenState().apply {
                when (action?.consume()) {
                    is AuthScreenState.Action.NavigateToHome -> navController.navigateToWatchList()
                    else -> Unit
                }
            })
        }

        composable(route = Route.WATCHLIST) {
            WatchListScreen(watchListScreenState = rememberWatchListScreenState().apply {
                when (val action = action?.consume()) {
                    is WatchListScreenState.Action.OnItemClicked -> navController.navigateToWatchListDetail(id = action.id)
                    is WatchListScreenState.Action.OnSignOutClicked -> navController.navigateToAuth()
                    else -> Unit
                }
            })
        }

        composable(route = Route.WATCHLIST_DETAIL) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getString("id") as String

            WatchListDetailScreen(screenState = rememberWatchListDetailScreenState(id = id).apply {
                when (action?.consume()) {
                    is WatchListDetailScreenState.Action.NavigateUp -> navController.navigateUp()
                    else -> Unit
                }
            })
        }
    }
}

fun NavHostController.navigateToAuth() {
    navigate(route = Route.AUTHENTICATION)
}

fun NavHostController.navigateToWatchList() {
    navigate(route = Route.WATCHLIST)
}

fun NavHostController.navigateToWatchListDetail(id: String) {
    navigate(route = "Watchlist/${id}")
}