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
            onLoadMoreExchanges = { viewModel.refreshData(false) }
        )
        binding.recyclerview.adapter = exchangeAdapter
        viewModel.listItems.onEach(exchangeAdapter::submitList).launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.logExchangeDetails.onEach(::logExchangeDetails).launchIn(viewLifecycleOwner.lifecycleScope)
        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.refreshData(true) }
    }

    private fun logExchangeDetails(event: ExchangeViewModel.Event) =
        Log.d("Details", (event as ExchangeViewModel.Event.LogExchangeId).id)
}
