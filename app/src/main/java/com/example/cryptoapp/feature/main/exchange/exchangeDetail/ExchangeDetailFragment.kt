package com.example.cryptoapp.feature.main.exchange.exchangeDetail

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.cryptoapp.BR
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentExchangeDetailBinding
import com.example.cryptoapp.feature.main.exchange.exchangeDetail.catalog.ExchangeChartPlaceHolder
import com.example.cryptoapp.feature.main.exchange.exchangeDetail.catalog.ExchangeDetailBody
import com.example.cryptoapp.feature.main.exchange.exchangeDetail.catalog.ExchangeDetailCardHolder
import com.example.cryptoapp.feature.main.exchange.exchangeDetail.catalog.ExchangeDetailChart
import com.example.cryptoapp.feature.main.exchange.exchangeDetail.catalog.ExchangeDetailChipGroup
import com.example.cryptoapp.feature.main.exchange.exchangeDetail.catalog.ExchangeDetailHeader
import com.example.cryptoapp.feature.main.exchange.exchangeDetail.catalog.ExchangeDetailItem
import com.example.cryptoapp.feature.shared.navigation.BaseFragment
import com.example.cryptoapp.feature.shared.utils.BundleArgumentDelegate
import com.example.cryptoapp.feature.shared.utils.createSnackBar
import com.example.cryptoapp.feature.shared.utils.getFormattedHour
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ExchangeDetailFragment : BaseFragment<FragmentExchangeDetailBinding>(R.layout.fragment_exchange_detail) {
    private val viewModel: ExchangeDetailViewModel by viewModel { parametersOf(arguments?.exchangeId) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.setVariable(BR.viewModel, viewModel)
        binding.fragmentExchangeDetail.setContent {
            ExchangeDetailScreen()
        }
    }

    @Composable
    private fun ExchangeDetailScreen() {

        // Todo: add somewhere into theme
        val contentColor = if (isSystemInDarkTheme()) colorResource(id = R.color.dark_mode_text_color) else Color.Black
        val backgroundColor = if (isSystemInDarkTheme()) colorResource(id = R.color.dark_mode_background_color) else Color.White

        if(viewModel.exchangeDetails != null ){
            ScreenContent(
                backgroundColor = backgroundColor,
                contentColor = contentColor
            )
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                SwipeRefreshIndicator(
                    state = rememberSwipeRefreshState(isRefreshing = viewModel.exchangeDetails == null),
                    refreshTriggerDistance = 80.dp,
                    modifier = Modifier.align(alignment = Alignment.TopCenter)
                )
            }
        }

        when (val event = viewModel.screenState) {
            is ExchangeDetailViewModel.ScreenState.ShowFirstErrorMessage -> ErrorContent(
                backgroundColor = backgroundColor,
                contentColor = contentColor
            )
            is ExchangeDetailViewModel.ScreenState.ShowSnackBarError -> binding.root.createSnackBar(message = event.message) {
                viewModel.refreshData()
            }
            else -> Unit
        }
    }

    @Composable
    private fun ErrorContent(backgroundColor: Color, contentColor: Color) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 150.dp)
                .padding(all = 8.dp),
            backgroundColor = backgroundColor
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.something_went_wrong),
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Spacer(modifier = Modifier.height(height = 8.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 40.dp)
                        .wrapContentWidth(),
                    onClick = { viewModel.refreshData() },
                    content = {
                        Text(text = stringResource(id = R.string.try_again))
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        backgroundColor = colorResource(id = R.color.orange)
                    )
                )
            }
        }
    }

    @Composable
    private fun ScreenContent(
        backgroundColor: Color,
        contentColor: Color
    ) {
        val detail = viewModel.exchangeDetails!!

        SwipeRefresh(
            state = rememberSwipeRefreshState(viewModel.screenState is ExchangeDetailViewModel.ScreenState.Loading),
            onRefresh = { viewModel.refreshData() },
        ) {
            LazyColumn {
                item {
                    ExchangeDetailHeader(
                        title = detail.name,
                        url = detail.image,
                        contentColor = contentColor
                    )
                }
                item {
                    ExchangeDetailCardHolder(
                        backgroundColor = colorResource(id = R.color.dark_mode_background_color),
                        contentColor = contentColor,
                        items = listOf(detail.trustScoreRank, detail.centralized, detail.trustScore)
                    )
                }
                item {
                    if (viewModel.exchangeHistory == null) {
                        ExchangeChartPlaceHolder(
                            backgroundColor = backgroundColor,
                            contentColor = contentColor
                        )
                    } else {
                        ExchangeDetailChart(lineDataSet = viewModel.exchangeHistory!!.dataSet)
                    }
                }
                item {
                    ExchangeDetailChipGroup(
                        backgroundColor = backgroundColor,
                        contentColor = contentColor,
                        items = listOf(stringResource(id = R.string.chip_24hr), stringResource(id = R.string.chip_1w)),
                        onClick = viewModel::onChipClicked
                    )
                }
                item {
                    ExchangeDetailBody(
                        tradeVolume = detail.tradeVolume24HBtc,
                        time = System.currentTimeMillis().getFormattedHour(),
                        tickers = detail.tickers.size.toString(),
                        contentColor = Color.LightGray
                    )
                }
                if (detail.yearEstablished.isNotEmpty()) {
                    item {
                        ExchangeDetailItem(
                            title = "Year established",
                            text = detail.yearEstablished,
                            contentColor = contentColor,
                            onClick = {}
                        )
                    }
                }
                if (detail.country.isNotEmpty()) {
                    item {
                        ExchangeDetailItem(
                            title = "Country",
                            text = detail.country,
                            contentColor = contentColor,
                            onClick = {}
                        )
                    }
                }
                if (detail.url.isNotEmpty()) {
                    item {
                        ExchangeDetailItem(
                            title = "Homepage",
                            text = detail.url,
                            contentColor = contentColor,
                            onClick = {}
                        )
                    }
                }
                if (detail.facebookURL.isNotEmpty()) {
                    item {
                        ExchangeDetailItem(
                            title = "Facebook",
                            text = detail.facebookURL,
                            contentColor = contentColor,
                            onClick = {}
                        )
                    }
                }
                if (detail.redditURL.isNotEmpty()) {
                    item {
                        ExchangeDetailItem(
                            title = "Reddit",
                            text = detail.redditURL,
                            contentColor = contentColor,
                            onClick = {}
                        )
                    }
                }
                if (detail.otherURL1.isNotEmpty()) {
                    item {
                        ExchangeDetailItem(
                            title = "Other URL",
                            text = detail.otherURL1,
                            contentColor = contentColor,
                            onClick = {}
                        )
                    }
                }
                if (detail.otherURL2.isNotEmpty()) {
                    item {
                        ExchangeDetailItem(
                            title = "Other URL",
                            text = detail.otherURL2,
                            contentColor = contentColor,
                            onClick = {}
                        )
                    }
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
