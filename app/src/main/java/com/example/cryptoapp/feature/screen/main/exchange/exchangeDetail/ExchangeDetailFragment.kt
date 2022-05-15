package com.example.cryptoapp.feature.screen.main.exchange.exchangeDetail

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.dimensionResource
import com.example.cryptoapp.BR
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentExchangeDetailBinding
import com.example.cryptoapp.feature.screen.main.MainFragment
import com.example.cryptoapp.feature.screen.main.exchange.exchangeDetail.catalog.ExchangeDetailBody
import com.example.cryptoapp.feature.screen.main.exchange.exchangeDetail.catalog.ExchangeDetailCardHolder
import com.example.cryptoapp.feature.screen.main.exchange.exchangeDetail.catalog.ExchangeDetailChart
import com.example.cryptoapp.feature.screen.main.exchange.exchangeDetail.catalog.ExchangeDetailChipGroup
import com.example.cryptoapp.feature.screen.main.exchange.exchangeDetail.catalog.ExchangeDetailHeader
import com.example.cryptoapp.feature.screen.main.exchange.exchangeDetail.catalog.ExchangeDetailItem
import com.example.cryptoapp.feature.shared.catalog.ErrorContent
import com.example.cryptoapp.feature.shared.catalog.LoadingIndicator
import com.example.cryptoapp.feature.shared.navigation.BaseFragment
import com.example.cryptoapp.feature.shared.utils.BundleArgumentDelegate
import com.example.cryptoapp.feature.shared.utils.createSnackBar
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ExchangeDetailFragment : BaseFragment<FragmentExchangeDetailBinding>(R.layout.fragment_exchange_detail) {
    private val viewModel: ExchangeDetailViewModel by viewModel { parametersOf(arguments?.exchangeId) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.setVariable(BR.viewModel, viewModel)
        (parentFragment as MainFragment).setAppBarTitle(title = view.context.getString(R.string.detail))
        binding.fragmentExchangeDetail.apply {
            // Dispose of the Composition when the view's LifecycleOwner is destroyed
            setViewCompositionStrategy(strategy = ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ExchangeDetailScreen()
            }
        }
    }

    @Composable
    private fun ExchangeDetailScreen() {
        if(viewModel.exchangeDetails != null) ScreenContent() else LoadingIndicator(isRefreshing = viewModel.exchangeDetails == null)

        when (val state = viewModel.screenState) {
            is ExchangeDetailViewModel.ScreenState.ShowFirstLoadingError ->
                ErrorContent(onClick = { viewModel.refreshData() })
            is ExchangeDetailViewModel.ScreenState.ShowSnackBarError ->
                binding.root.createSnackBar(message = state.message, snackBarAction = viewModel::refreshData)
            else -> Unit
        }
    }

    @Composable
    private fun ScreenContent() {
        val details = viewModel.exchangeDetails!!

        SwipeRefresh(
            state = rememberSwipeRefreshState(viewModel.screenState is ExchangeDetailViewModel.ScreenState.Loading),
            onRefresh = viewModel::refreshData,
        ) {
            LazyColumn {
                item {
                    ExchangeDetailHeader(title = details.name, url = details.image)
                }
                item {
                    ExchangeDetailCardHolder(items = listOf(details.trustScoreRank, details.centralized, details.trustScore))
                }
                item {
                    ExchangeDetailChart(history = viewModel.exchangeHistory, unitOfTimeType = viewModel.unitOfTimeType)
                }
                item {
                    ExchangeDetailChipGroup(onClick = viewModel::onChipClicked)
                }
                item {
                    ExchangeDetailBody(
                        tradeVolume = details.tradeVolume24HBtc,
                        tickers = details.tickers.size.toString(),
                        modifier = Modifier.padding(
                            horizontal = dimensionResource(id = R.dimen.content_padding)
                        )
                    )
                }
                items(details.generalDetails) { generalDetail ->
                    ExchangeDetailItem(title = generalDetail.title, text = generalDetail.value)
                }
            }
        }
    }

    companion object {
        private var Bundle.exchangeId by BundleArgumentDelegate.String(key = "exchange_id", defaultValue = "")

        fun newInstance(exchangeId: String) = ExchangeDetailFragment().apply {
            arguments = Bundle().apply {
                this.exchangeId = exchangeId
            }
        }
    }
}
