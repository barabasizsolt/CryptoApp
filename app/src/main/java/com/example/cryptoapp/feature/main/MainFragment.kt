package com.example.cryptoapp.feature.main

import android.os.Bundle
import android.view.View
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentMainBinding
import com.example.cryptoapp.feature.main.cryptocurrency.cryptocurrencyList.CryptoCurrencyFragment
import com.example.cryptoapp.feature.main.exchange.ExchangeFragment
import com.example.cryptoapp.feature.main.news.NewsFragment
import com.example.cryptoapp.feature.main.user.ProfileFragment
import com.example.cryptoapp.feature.shared.navigation.BaseFragment
import com.example.cryptoapp.feature.shared.utils.consume
import com.example.cryptoapp.feature.shared.utils.handleReplace

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.currencies -> consume {
                    childFragmentManager.handleReplace(
                        tag = "currencies",
                        newInstance = CryptoCurrencyFragment.Companion::newInstance,
                        addToBackStack = true
                    )
                }
                R.id.exchanges -> consume {
                    childFragmentManager.handleReplace(
                        tag = "exchanges",
                        newInstance = ExchangeFragment.Companion::newInstance,
                        addToBackStack = true
                    )
                }
                R.id.news -> consume {
                    childFragmentManager.handleReplace(
                        tag = "news",
                        newInstance = NewsFragment.Companion::newInstance,
                        addToBackStack = true
                    )
                }
                else -> false
            }
        }
        if (savedInstanceState == null && currentFragment == null) {
            binding.bottomNavigationView.selectedItemId = R.id.currencies
        }
        binding.topBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> consume {
                    childFragmentManager.handleReplace(
                        tag = "profile",
                        newInstance = ProfileFragment.Companion::newInstance,
                        addToBackStack = true
                    )
                }
                else -> false
            }
        }
    }

    override fun onBackPressed(): Boolean = when (currentFragment?.childFragmentManager?.backStackEntryCount ?: 0 <= 1) {
        true -> when {
            binding.bottomNavigationView.selectedItemId == R.id.currencies -> when {
                getString(R.string.crypto_details_back_stack_tag).equalsTopBackStackName() -> childFragmentManager.popBackStackImmediate()
                getString(R.string.profile_back_stack_tag).equalsTopBackStackName() -> consume { binding.bottomNavigationView.selectedItemId = R.id.currencies }
                else -> false
            }
            getString(R.string.profile_back_stack_tag).equalsTopBackStackName() -> childFragmentManager.popBackStackImmediate()
            else -> consume { binding.bottomNavigationView.selectedItemId = R.id.currencies }
        }
        else -> super.onBackPressed()
    }

    private fun String.equalsTopBackStackName() = childFragmentManager.getBackStackEntryAt(childFragmentManager.backStackEntryCount - 1).name.equals(this)

    companion object {
        fun newInstance() = MainFragment()
    }
}
