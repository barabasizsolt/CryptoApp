package com.example.cryptoapp.feature.main.home

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentHomeBinding
import com.example.cryptoapp.feature.main.home.catalog.CryptoCurrencyCard
import com.example.cryptoapp.feature.main.home.catalog.CryptoCurrencyCardHolder
import com.example.cryptoapp.feature.shared.navigation.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.homeScreen.setContent {
            CryptoCurrencyCardHolder(modifier = Modifier.padding(all = 20.dp))
        }
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}