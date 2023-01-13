package com.barabasizsolt.feature.screen.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.barabasizsolt.feature.R
import com.barabasizsolt.feature.databinding.FragmentMarketBinding
import com.barabasizsolt.feature.screen.main.category.CategoryFragment
import com.barabasizsolt.feature.screen.main.cryptocurrency.cryptocurrencyList.CryptoCurrencyFragment
import com.barabasizsolt.feature.screen.main.exchange.exchangeList.ExchangeFragment
import com.barabasizsolt.feature.shared.navigation.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator

class MarketFragment : BaseFragment<FragmentMarketBinding>(R.layout.fragment_market) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewPager.adapter = ViewPagerAdapter(fragment = this)
        (parentFragment as MainFragment).setAppBarTitle(title = view.context.getString(R.string.market))
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.crypto_currency)
                    tab.select()
                }
                1 -> {
                    tab.text = getString(R.string.exchanges)
                }
                2 -> {
                    tab.text = getString(R.string.categories)
                }
            }
        }.attach()
    }

    class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> CryptoCurrencyFragment.newInstance()
            1 -> ExchangeFragment.newInstance()
            2 -> CategoryFragment.newInstance()
            else -> throw IllegalStateException("Invalid position: $position.")
        }
    }

    companion object {
        fun newInstance() = MarketFragment()
    }
}
