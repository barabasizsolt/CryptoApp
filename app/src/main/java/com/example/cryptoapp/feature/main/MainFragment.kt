package com.example.cryptoapp.feature.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentMainBinding
import com.example.cryptoapp.feature.main.cryptocurrency.cryptocurrencyDetails.CryptoCurrencyDetailsFragment
import com.example.cryptoapp.feature.main.cryptocurrency.cryptocurrencyList.CryptoCurrencyFragment
import com.example.cryptoapp.feature.main.exchange.ExchangeFragment
import com.example.cryptoapp.feature.main.news.NewsFragment
import com.example.cryptoapp.feature.shared.navigation.BaseFragment
import com.example.cryptoapp.feature.shared.utils.consume
import com.example.cryptoapp.feature.shared.utils.handleReplace

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.currencies -> consume {
                    navigator?.run {
                        childFragmentManager.handleReplace(
                            tag = "currencies",
                            newInstance = CryptoCurrencyFragment.Companion::newInstance,
                            addToBackStack = true
                        )
                    }
                }
                R.id.exchanges -> consume {
                    navigator?.run {
                        childFragmentManager.handleReplace(
                            tag = "exchanges",
                            newInstance = ExchangeFragment.Companion::newInstance,
                            addToBackStack = true
                        )
                    }
                }
                R.id.news -> consume {
                    navigator?.run {
                        childFragmentManager.handleReplace(
                            tag = "news",
                            newInstance = NewsFragment.Companion::newInstance,
                            addToBackStack = true
                        )
                    }
                }
                else -> false
            }
        }
        if (savedInstanceState == null && currentFragment == null) {
            binding.bottomNavigationView.selectedItemId = R.id.currencies
        }
    }

    override fun onBackPressed(): Boolean = if (currentFragment?.childFragmentManager?.backStackEntryCount ?: 0 <= 1) {
        if(binding.bottomNavigationView.selectedItemId == R.id.currencies) {
            if ((childFragmentManager.getBackStackEntryAt(childFragmentManager.backStackEntryCount - 1).name.equals(getString(R.string.crypto_details_back_stack_tag)))) {
                childFragmentManager.popBackStackImmediate()
            } else {
                false
            }
        } else consume {
            binding.bottomNavigationView.selectedItemId = R.id.currencies
        }
    } else {
        super.onBackPressed()
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
