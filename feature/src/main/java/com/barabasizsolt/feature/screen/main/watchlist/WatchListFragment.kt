package com.barabasizsolt.feature.screen.main.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.example.cryptoapp.R
import com.example.cryptoapp.feature.screen.main.MainFragment
import com.example.cryptoapp.feature.screen.main.watchlist.catalog.CryptoCurrencyItem
import com.google.android.material.composethemeadapter.MdcTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.fragment.app.Fragment
import com.example.cryptoapp.feature.screen.main.cryptocurrency.cryptocurrencyDetails.CryptoCurrencyDetailsFragment
import com.example.cryptoapp.feature.screen.main.watchlist.catalog.DeleteItem
import com.example.cryptoapp.feature.screen.main.watchlist.catalog.WatchListPlaceHolder
import com.example.cryptoapp.feature.screen.main.watchlist.catalog.WatchListSummary
import com.example.cryptoapp.feature.shared.catalog.ErrorContent
import com.example.cryptoapp.feature.shared.catalog.LoadingIndicator
import com.example.cryptoapp.feature.shared.utils.convertToCompactPrice
import com.example.cryptoapp.feature.shared.utils.convertToPrice
import com.example.cryptoapp.feature.shared.utils.createSnackBar
import com.example.cryptoapp.feature.shared.utils.handleReplace

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
                (parentFragment as MainFragment).setAppBarTitle(title = LocalContext.current.getString(R.string.watch_list))
                MdcTheme {
                    WatchListScreen(viewModel = viewModel)
                }
            }
        }
    }

    @Composable
    private fun WatchListScreen(viewModel: WatchListViewModel) {

        when (viewModel.screenState) {
            is WatchListViewModel.ScreenState.Loading -> LoadingIndicator(isRefreshing = true)
            else -> Unit
        }

        when (val state = viewModel.screenState) {
            is WatchListViewModel.ScreenState.ShowFirstLoadingError -> ErrorContent(onClick = { viewModel.refreshData() })
            is WatchListViewModel.ScreenState.ShowSnackBarError -> LocalView.current.createSnackBar(message = state.message, snackBarAction = viewModel::refreshData)
            else -> ScreenContent(viewModel = viewModel)
        }

        when (val action = viewModel.action) {
            is WatchListViewModel.Action.OnItemClicked -> parentFragmentManager.handleReplace(
                addToBackStack = true,
                newInstance = { CryptoCurrencyDetailsFragment.newInstance(action.id) },
                tag = "${action.id}_${getString(R.string.crypto_details_back_stack_tag)}"
            ).also { viewModel.reset() }
            else -> Unit
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun ScreenContent(viewModel: WatchListViewModel) {
        if (viewModel.cryptoCurrencies.isEmpty() && viewModel.screenState !is WatchListViewModel.ScreenState.Loading) {
            WatchListPlaceHolder(onClick = { (parentFragment as MainFragment).navigateToCryptoCurrencies() })
        } else {
            viewModel.watchListSummary?.let { summary ->
                SwipeRefresh(
                    state = rememberSwipeRefreshState(viewModel.screenState is WatchListViewModel.ScreenState.Loading),
                    onRefresh = viewModel::refreshData,
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.content_padding)),
                        contentPadding = PaddingValues(all = dimensionResource(id = R.dimen.content_padding))
                    ) {
                        item {
                            WatchListSummary(
                                totalPrice = summary.totalPrice,
                                iconUrl = summary.mvpCoinUrl,
                                totalCoin = summary.totalCoin,
                                totalChange = summary.totalChange,
                                totalVolume = summary.totalVolume,
                                totalMarketCap = summary.totalMarketCap,
                                onClick = { (parentFragment as MainFragment).navigateToCryptoCurrencies() }
                            )
                        }
                        items(
                            items = viewModel.cryptoCurrencies,
                            key = { it.uuid }
                        ) { item ->

                            val dismissState = rememberDismissState(
                                confirmStateChange = { dismissValue ->
                                    if (dismissValue == DismissValue.DismissedToStart) {
                                        viewModel.deleteCryptoCurrency(uid = item.uuid)
                                    }
                                    true
                                }
                            )

                            SwipeToDismiss(
                                state = dismissState,
                                background = {
                                    val color = when (dismissState.dismissDirection) {
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
                                        marketCap = item.marketCap.convertToCompactPrice(),
                                        onClick = { viewModel.onItemClicked(id = item.uuid) }
                                    )
                                },
                                directions = setOf(element = DismissDirection.EndToStart)
                            )
                        }
                    }
                }
            }
        }
    }
    
    companion object {
        fun newInstance() = WatchListFragment()
    }
}