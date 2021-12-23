package com.example.cryptoapp.feature.exchange

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoapp.databinding.FragmentExchangeBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExchangeFragment : Fragment() {

    private lateinit var binding: FragmentExchangeBinding
    private val viewModel by viewModel<ExchangeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExchangeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        is ExchangeViewModel.Event.ErrorEvent -> createErrorSnackBar(event)
        is ExchangeViewModel.Event.LogExchangeIdEvent -> logExchangeDetails(event)
    }

    private fun logExchangeDetails(event: ExchangeViewModel.Event.LogExchangeIdEvent) {
        Log.d("Details", event.id)
    }

    private fun createErrorSnackBar(errorEvent: ExchangeViewModel.Event.ErrorEvent) {
        Snackbar.make(binding.root, errorEvent.errorMessage, Snackbar.LENGTH_LONG)
            .setAction("Retry") {
                viewModel.refreshData(isForceRefresh = true)
            }
            .show()
    }
}
