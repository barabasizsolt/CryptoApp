package com.example.cryptoapp.feature.main.exchange.exchangeDetail

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.example.cryptoapp.BR
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentExchangeDetailBinding
import com.example.cryptoapp.feature.main.exchange.exchangeDetail.catalog.ExchangeDetailBody
import com.example.cryptoapp.feature.main.exchange.exchangeDetail.catalog.ExchangeDetailCardHolder
import com.example.cryptoapp.feature.main.exchange.exchangeDetail.catalog.ExchangeDetailChart
import com.example.cryptoapp.feature.main.exchange.exchangeDetail.catalog.ExchangeDetailChipGroup
import com.example.cryptoapp.feature.main.exchange.exchangeDetail.catalog.ExchangeDetailHeader
import com.example.cryptoapp.feature.main.exchange.exchangeDetail.catalog.ExchangeDetailItem
import com.example.cryptoapp.feature.shared.navigation.BaseFragment
import com.example.cryptoapp.feature.shared.utils.BundleArgumentDelegate
import com.example.cryptoapp.feature.shared.utils.getFormattedHour
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ExchangeDetailFragment : BaseFragment<FragmentExchangeDetailBinding>(R.layout.fragment_exchange_detail) {
    private val viewModel: ExchangeDetailViewModel by viewModel { parametersOf(arguments?.exchangeId) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.setVariable(BR.viewModel, viewModel)
        binding.fragmentExchangeDetail.setContent {

            when (viewModel.screenState) {
                is ExchangeDetailViewModel.ScreenState.Normal -> ScreenContent()
                is ExchangeDetailViewModel.ScreenState.Loading -> {
                    //Loading indicator
                }
                else -> Unit
            }
            when (val event = viewModel.event) {
                is ExchangeDetailViewModel.Event.ShowFirstErrorMessage -> {}
                is ExchangeDetailViewModel.Event.ShowSnackBarError -> {}
                else -> Unit
            }
        }
    }

    @Composable
    private fun ScreenContent() {
        val detail = viewModel.exchangeDetails!!
        val history = viewModel.exchangeHistory!!

        // Todo: add somewhere into theme
        val contentColor = if (isSystemInDarkTheme()) colorResource(id = R.color.dark_mode_text_color) else Color.Black
        val backgroundColor = if (isSystemInDarkTheme()) colorResource(id = R.color.dark_mode_background_color) else Color.White

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
                ExchangeDetailChart(lineDataSet = history.dataSet)
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

    companion object {
        private var Bundle.exchangeId by BundleArgumentDelegate.String(key = "exchange_id", defaultValue = "")

        fun newInstance(exchangeId: String) = ExchangeDetailFragment().apply {
            arguments = Bundle().apply {
                this.exchangeId = exchangeId
            }
        }
    }
}
