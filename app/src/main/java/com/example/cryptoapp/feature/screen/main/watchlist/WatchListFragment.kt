package com.example.cryptoapp.feature.screen.main.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import com.example.cryptoapp.BR
import com.example.cryptoapp.R
import com.example.cryptoapp.feature.screen.main.MainFragment
import com.example.cryptoapp.feature.screen.main.watchlist.catalog.CryptoCurrencyItem
import com.example.cryptoapp.feature.shared.navigation.BaseFragment
import com.google.android.material.composethemeadapter.MdcTheme
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import com.example.cryptoapp.data.model.cryptocurrency.CryptoCurrency
import com.example.cryptoapp.feature.screen.main.exchange.exchangeDetail.ExchangeDetailViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalView
import androidx.fragment.app.Fragment
import com.example.cryptoapp.feature.screen.main.MarketFragment
import com.example.cryptoapp.feature.screen.main.watchlist.catalog.DeleteItem
import com.example.cryptoapp.feature.screen.main.watchlist.catalog.WatchListPlaceHolder
import com.example.cryptoapp.feature.shared.catalog.ErrorContent
import com.example.cryptoapp.feature.shared.catalog.LoadingIndicator
import com.example.cryptoapp.feature.shared.utils.convertToCompactPrice
import com.example.cryptoapp.feature.shared.utils.convertToPrice
import com.example.cryptoapp.feature.shared.utils.createSnackBar
import com.example.cryptoapp.feature.shared.utils.formatInput

class WatchListFragment : Fragment() {
    private val viewModel: WatchListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(context = requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MdcTheme {
                    WatchListScreen(viewModel = viewModel)
                }
            }
        }
    }

    @Composable
    private fun WatchListScreen(viewModel: WatchListViewModel) {

        val cryptoCurrencies = viewModel.cryptoCurrencies

        if(cryptoCurrencies != null) {
            if (cryptoCurrencies.isNotEmpty()) {
                ScreenContent(viewModel = viewModel)
            } else {
                WatchListPlaceHolder(
                    onClick = { (parentFragment as MainFragment).navigateToCryptoCurrencies() }
                )
            }
        } else {
            LoadingIndicator(isRefreshing = viewModel.screenState is WatchListViewModel.ScreenState.Loading)
        }

        when (val state = viewModel.screenState) {
            is WatchListViewModel.ScreenState.ShowFirstLoadingError ->
                ErrorContent(onClick = { viewModel.refreshData() })
            is WatchListViewModel.ScreenState.ShowSnackBarError ->
                LocalView.current.createSnackBar(message = state.message, snackBarAction = viewModel::refreshData)
            else -> Unit
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun ScreenContent(viewModel: WatchListViewModel) {
        viewModel.cryptoCurrencies?.let { cryptoCurrencies ->
            SwipeRefresh(
                state = rememberSwipeRefreshState(viewModel.screenState is WatchListViewModel.ScreenState.Loading),
                onRefresh = viewModel::refreshData,
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.content_padding)),
                    contentPadding = PaddingValues(all = dimensionResource(id = R.dimen.content_padding))
                ) {
                    items(
                        items = cryptoCurrencies,
                        key = { it.uuid }
                    ) { item ->

                        val dismissState = rememberDismissState(
                            confirmStateChange = { dismissValue ->
                                if (dismissValue == DismissValue.DismissedToStart) { viewModel.deleteCryptoCurrency(uid = item.uuid) }
                                true
                            }
                        )

                        SwipeToDismiss(
                            state = dismissState,
                            background = {
                                val color = when (dismissState.dismissDirection){
                                    DismissDirection.EndToStart -> MaterialTheme.colors.primary
                                    else -> Color.Transparent
                                }
                                DeleteItem(color = color)
                            },
                            dismissContent = {
                                CryptoCurrencyItem(
                                    iconUrl = item.iconUrl,
                                    name = item.name,
                                    symbol = item.symbol,
                                    price = item.price.convertToPrice(),
                                    change = item.change,
                                    volume = item.volume.convertToCompactPrice(),
                                    marketCap = item.marketCap.formatInput()
                                )
                            },
                            directions = setOf(element = DismissDirection.EndToStart)
                        )
                    }
                }
            }
        }
    }
    
    companion object {
        fun newInstance() = WatchListFragment()
    }
}