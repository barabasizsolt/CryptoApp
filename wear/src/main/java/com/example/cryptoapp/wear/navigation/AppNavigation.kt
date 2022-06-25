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
import com.example.cryptoapp.wear.screen.cryptocurrency.watchlist.WatchListScreen
import com.example.cryptoapp.wear.screen.cryptocurrency.watchlist.WatchListScreenState
import com.example.cryptoapp.wear.screen.cryptocurrency.watchlist.rememberWatchListScreenState
import com.example.cryptoapp.wear.screen.profile.ProfileScreen
import com.example.cryptoapp.wear.screen.profile.ProfileScreenState
import com.example.cryptoapp.wear.screen.profile.rememberProfileScreenState
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
                    is AuthScreenState.Action.NavigateToHome -> navController.navigateToWatchList()
                    else -> Unit
                }
            })
        }

        composable(route = Route.WATCHLIST) {
            WatchListScreen(watchListScreenState = rememberWatchListScreenState().apply {
                when (action?.consume()) {
                    is WatchListScreenState.Action.OnItemClicked -> {
                        //TODO: somehow open the fcking app
                    }
                    is WatchListScreenState.Action.OnProfileClicked -> navController.navigateToProfile()
                    else -> Unit
                }
            })
        }

        composable(route = Route.PROFILE) {
            ProfileScreen(screenState = rememberProfileScreenState().apply {
                when (action?.consume()) {
                    is ProfileScreenState.Action.SignOut -> navController.navigateToAuth()
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

fun NavHostController.navigateToProfile() {
    navigate(route = Route.PROFILE)
}