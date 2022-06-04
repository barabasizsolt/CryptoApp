package com.example.cryptoapp.feature.screen.main.watchlist

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import com.example.cryptoapp.BR
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentWatchListBinding
import com.example.cryptoapp.feature.screen.main.MainFragment
import com.example.cryptoapp.feature.screen.main.watchlist.catalog.CryptoCurrencyItem
import com.example.cryptoapp.feature.shared.navigation.BaseFragment
import com.google.android.material.composethemeadapter.MdcTheme

class WatchListFragment : BaseFragment<FragmentWatchListBinding>(R.layout.fragment_watch_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //binding.setVariable(BR.viewModel, viewModel)
        (parentFragment as MainFragment).setAppBarTitle(title = view.context.getString(R.string.detail))
        binding.fragmentWatchList.apply {
            setViewCompositionStrategy(strategy = ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MdcTheme {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
                    ){
                        items(count = 20) {
                            CryptoCurrencyItem(
                                iconUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                                name = "Bitcoin",
                                symbol = "BTC",
                                price = "$29,624.18",
                                change = "-2.36%"
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