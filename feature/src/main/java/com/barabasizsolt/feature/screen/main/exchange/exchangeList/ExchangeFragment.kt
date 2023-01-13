package com.barabasizsolt.feature.screen.main.exchange.exchangeList

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.barabasizsolt.feature.R
import com.barabasizsolt.feature.databinding.FragmentExchangeBinding
import com.barabasizsolt.feature.screen.main.exchange.exchangeDetail.ExchangeDetailFragment
import com.barabasizsolt.feature.shared.navigation.BaseFragment
import com.barabasizsolt.feature.shared.utils.createSnackBar
import com.barabasizsolt.feature.shared.utils.handleReplace
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
        is ExchangeViewModel.Event.OpenDetailPage -> parentFragment?.parentFragmentManager?.handleReplace(
            addToBackStack = true,
            newInstance = { ExchangeDetailFragment.newInstance(exchangeId = event.id.substringAfter("_")) },
            tag = "${event.id}_${getString(R.string.exchange_details_back_stack_tag)}"
        )
    }

    companion object {
        fun newInstance() = ExchangeFragment()
    }
}
