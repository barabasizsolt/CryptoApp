package com.example.cryptoapp.feature.main.exchange.exchangeDetail

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cryptoapp.BR
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentExchangeDetailBinding
import com.example.cryptoapp.feature.main.cryptocurrency.cryptocurrencyDetails.CryptoCurrencyDetailsViewModel
import com.example.cryptoapp.feature.main.exchange.exchangeDetail.catalog.ExchangeDetailBody
import com.example.cryptoapp.feature.main.exchange.exchangeDetail.catalog.ExchangeDetailCardHolder
import com.example.cryptoapp.feature.main.exchange.exchangeDetail.catalog.ExchangeDetailChart
import com.example.cryptoapp.feature.main.exchange.exchangeDetail.catalog.ExchangeDetailHeader
import com.example.cryptoapp.feature.main.exchange.exchangeDetail.catalog.ExchangeDetailItem
import com.example.cryptoapp.feature.shared.navigation.BaseFragment
import com.example.cryptoapp.feature.shared.utils.BundleArgumentDelegate
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ExchangeDetailFragment : BaseFragment<FragmentExchangeDetailBinding>(R.layout.fragment_exchange_detail) {
    private val viewModel: ExchangeDetailViewModel by viewModel { parametersOf(arguments?.exchangeId) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.setVariable(BR.viewModel, viewModel)
        binding.fragmentExchangeDetail.setContent {

            LazyColumn() {
                item {
                    ExchangeDetailHeader(
                        title = "Binance",
                        url = "https://assets.coingecko.com/markets/images/52/small/binance.jpg?1519353250",
                        contentColor = Color.LightGray
                    )
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Gray,
                        thickness = 0.7.dp
                    )
                }
                item {
                    ExchangeDetailCardHolder(
                        backgroundColor = colorResource(id = R.color.dark_mode_background_color),
                        contentColor = Color.LightGray,
                        items = listOf("Rank #1", "Centralized", "Trust 10/10")
                    )
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Gray,
                        thickness = 0.7.dp
                    )
                }
                item {
                    ExchangeDetailChart()
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Gray,
                        thickness = 0.7.dp
                    )
                }
                item {
                    ExchangeDetailCardHolder(
                        backgroundColor = colorResource(id = R.color.dark_mode_background_color),
                        contentColor = Color.LightGray,
                        items = listOf("24 Hours", "7 Days", "1 Year", "Max"),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        fontWeight = FontWeight.Light
                    )
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Gray,
                        thickness = 0.7.dp
                    )
                }
                item {
                    ExchangeDetailBody(
                        tradeVolume = "158,039.31",
                        time = "3:25",
                        tickers = "195",
                        contentColor = Color.LightGray
                    )
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Gray,
                        thickness = 0.7.dp
                    )
                }
                items(count = 6) {
                    ExchangeDetailItem(
                        title = "Homepage",
                        text = "www.binance.com",
                        contentColor = Color.LightGray
                    )
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Gray,
                        thickness = 0.7.dp
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
