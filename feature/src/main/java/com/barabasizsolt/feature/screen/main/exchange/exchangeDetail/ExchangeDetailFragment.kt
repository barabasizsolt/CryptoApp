package com.barabasizsolt.feature.screen.main.exchange.exchangeDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.dimensionResource
import androidx.fragment.app.Fragment
import com.barabasizsolt.feature.R
import com.barabasizsolt.feature.screen.main.exchange.exchangeDetail.catalog.ExchangeDetailBody
import com.barabasizsolt.feature.screen.main.exchange.exchangeDetail.catalog.ExchangeDetailCardHolder
import com.barabasizsolt.feature.screen.main.exchange.exchangeDetail.catalog.ExchangeDetailChart
import com.barabasizsolt.feature.screen.main.exchange.exchangeDetail.catalog.ExchangeDetailChipGroup
import com.barabasizsolt.feature.screen.main.exchange.exchangeDetail.catalog.ExchangeDetailHeader
import com.barabasizsolt.feature.screen.main.exchange.exchangeDetail.catalog.ExchangeDetailItem
import com.barabasizsolt.feature.shared.catalog.ErrorContent
import com.barabasizsolt.feature.shared.catalog.LoadingIndicator
import com.barabasizsolt.feature.shared.utils.BundleArgumentDelegate
import com.barabasizsolt.feature.shared.utils.createSnackBar
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.material.composethemeadapter.MdcTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ExchangeDetailFragment : Fragment() {
    private val viewModel: ExchangeDetailViewModel by viewModel { parametersOf(arguments?.exchangeId) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(context = requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MdcTheme {
                    ExchangeDetailScreen(viewModel = viewModel)
                }
            }
        }
    }


    @Composable
    private fun ExchangeDetailScreen(viewModel: ExchangeDetailViewModel) {
        if (viewModel.exchangeDetails != null)
            ScreenContent(viewModel = viewModel) else LoadingIndicator(isRefreshing = true)

        when (val state = viewModel.screenState) {
            is ExchangeDetailViewModel.ScreenState.ShowFirstLoadingError ->
                ErrorContent(onClick = { viewModel.refreshData() })
            is ExchangeDetailViewModel.ScreenState.ShowSnackBarError ->
                LocalView.current.createSnackBar(message = state.message, snackBarAction = viewModel::refreshData)
            else -> Unit
        }
    }

    @Composable
    private fun ScreenContent(viewModel: ExchangeDetailViewModel) {

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
                    ExchangeDetailItem(
                        title = generalDetail.title,
                        text = generalDetail.value,
                        onClick = { if (generalDetail.clickable) startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(generalDetail.value))) }
                    )
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
