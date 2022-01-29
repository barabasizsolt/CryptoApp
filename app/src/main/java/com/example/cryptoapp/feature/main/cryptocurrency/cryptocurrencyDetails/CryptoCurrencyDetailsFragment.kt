package com.example.cryptoapp.feature.main.cryptocurrency.cryptocurrencyDetails

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentCryptoCurrencyDetailsBinding
import com.example.cryptoapp.feature.shared.navigation.BaseFragment
import com.example.cryptoapp.feature.shared.utils.BundleArgumentDelegate
import com.example.cryptoapp.feature.shared.utils.createErrorSnackBar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CryptoCurrencyDetailsFragment : BaseFragment<FragmentCryptoCurrencyDetailsBinding>(R.layout.fragment_crypto_currency_details) {
    private val viewModel: CryptoCurrencyDetailsViewModel by viewModel { parametersOf(arguments?.cryptoCurrencyId) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val detailsAdapter = CryptoCurrencyDetailsAdapter(
            onChipClicked = viewModel::onChipClicked,
            onDescriptionArrowClicked = viewModel::onDescriptionArrowClicked,
            onTryAgainButtonClicked = viewModel::refreshData
        )
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.let {
            it.adapter = detailsAdapter
            it.layoutManager = layoutManager
            it.addItemDecoration(DividerItemDecoration(requireContext(), layoutManager.orientation))
        }
        viewModel.listItem.onEach(detailsAdapter::submitList).launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.event.onEach(::listenToEvents).launchIn(viewLifecycleOwner.lifecycleScope)
        binding.swipeRefreshLayout.setOnRefreshListener(viewModel::refreshData)
    }

    private fun listenToEvents(event: CryptoCurrencyDetailsViewModel.Event) = when (event) {
        is CryptoCurrencyDetailsViewModel.Event.ShowErrorMessage -> binding.root.createErrorSnackBar(event.errorMessage) {
            viewModel.refreshData()
        }
    }

    companion object {
        private var Bundle.cryptoCurrencyId by BundleArgumentDelegate.String(key = "crypto_currency_id", defaultValue = "")

        fun newInstance(cryptoCurrencyId: String) = CryptoCurrencyDetailsFragment().apply {
            arguments = Bundle().apply {
                this.cryptoCurrencyId = cryptoCurrencyId
            }
        }
    }
}
