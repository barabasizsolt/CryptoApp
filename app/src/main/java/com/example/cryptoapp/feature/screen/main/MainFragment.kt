package com.example.cryptoapp.feature.screen.main

import android.os.Bundle
import android.view.View
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentMainBinding
import com.example.cryptoapp.feature.screen.main.news.NewsFragment
import com.example.cryptoapp.feature.screen.main.user.ProfileFragment
import com.example.cryptoapp.feature.screen.main.watchlist.WatchListFragment
import com.example.cryptoapp.feature.shared.navigation.BaseFragment
import com.example.cryptoapp.feature.shared.utils.consume
import com.example.cryptoapp.feature.shared.utils.handleReplace

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.market -> consume {
                    childFragmentManager.handleReplace(
                        tag = "market",
                        newInstance = MarketFragment.Companion::newInstance,
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
                R.id.watch_list -> consume {
                    childFragmentManager.handleReplace(
                        tag = "watch_list",
                        newInstance = WatchListFragment.Companion::newInstance,
                        addToBackStack = true
                    )
                }
                else -> false
            }
        }
        if (savedInstanceState == null && currentFragment == null) {
            binding.bottomNavigationView.selectedItemId = R.id.market
        }
        binding.topBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> consume {
                    childFragmentManager.handleReplace(
                        tag = getString(R.string.profile_back_stack_tag),
                        newInstance = ProfileFragment.Companion::newInstance,
                        addToBackStack = true
                    )
                }
                else -> false
            }
        }
    }

    fun setAppBarTitle(title: String) {
        binding.topBar.title = title
    }

    fun navigateToCryptoCurrencies() {
        binding.bottomNavigationView.selectedItemId = R.id.market
    }

    override fun onBackPressed(): Boolean = when (currentFragment?.childFragmentManager?.backStackEntryCount ?: 0 <= 1) {
        true -> when {
            binding.bottomNavigationView.selectedItemId == R.id.market -> when {
                getString(R.string.crypto_details_back_stack_tag).containsTopBackStackName() -> childFragmentManager.popBackStackImmediate()
                getString(R.string.exchange_details_back_stack_tag).containsTopBackStackName() -> childFragmentManager.popBackStackImmediate()
                getString(R.string.profile_back_stack_tag).containsTopBackStackName() -> childFragmentManager.popBackStackImmediate()
                else -> false
            }
            binding.bottomNavigationView.selectedItemId == R.id.watch_list -> when {
                getString(R.string.crypto_details_back_stack_tag).containsTopBackStackName() -> childFragmentManager.popBackStackImmediate()
                getString(R.string.profile_back_stack_tag).containsTopBackStackName() -> childFragmentManager.popBackStackImmediate()
                else -> consume { binding.bottomNavigationView.selectedItemId = R.id.market }
            }
            getString(R.string.profile_back_stack_tag).containsTopBackStackName() -> childFragmentManager.popBackStackImmediate()
            else -> consume { binding.bottomNavigationView.selectedItemId = R.id.market }
        }
        else -> super.onBackPressed()
    }

    private fun String.containsTopBackStackName() = childFragmentManager.getBackStackEntryAt(childFragmentManager.backStackEntryCount - 1).name!!.contains(this)

    companion object {
        fun newInstance() = MainFragment()
    }
}
