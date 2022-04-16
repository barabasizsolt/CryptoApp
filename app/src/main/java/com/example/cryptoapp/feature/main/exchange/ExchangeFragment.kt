package com.example.cryptoapp.feature.main.exchange

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoapp.BR
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentExchangeBinding
import com.example.cryptoapp.feature.shared.navigation.BaseFragment
import com.example.cryptoapp.feature.shared.utils.createSnackBar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExchangeFragment : BaseFragment<FragmentExchangeBinding>(R.layout.fragment_exchange) {

    private val viewModel by viewModel<ExchangeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.setVariable(BR.viewModel, viewModel)
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        val exchangeAdapter = ExchangeAdapter(
            onExchangeItemClick = viewModel::onExchangeItemClicked,
            onLoadMoreExchanges = { viewModel.refreshData(isForceRefresh = false) },
            onTryAgainButtonClicked = { viewModel.refreshData(isForceRefresh = true) }
        )
        binding.recyclerview.adapter = exchangeAdapter
        viewModel.listItems.onEach(exchangeAdapter::submitList).launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.event.onEach(::listenToEvents).launchIn(viewLifecycleOwner.lifecycleScope)
        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.refreshData(isForceRefresh = true) }
    }

    private fun listenToEvents(event: ExchangeViewModel.Event) = when (event) {
        is ExchangeViewModel.Event.ShowErrorMessage -> binding.root.createSnackBar(event.errorMessage) {
            viewModel.refreshData(isForceRefresh = true)
        }
        is ExchangeViewModel.Event.LogExchangeId -> logExchangeDetails(event)
    }

    private fun logExchangeDetails(event: ExchangeViewModel.Event.LogExchangeId) {
        Log.d("Details", event.id)
    }

    companion object {
        fun newInstance() = ExchangeFragment()
    }
}
