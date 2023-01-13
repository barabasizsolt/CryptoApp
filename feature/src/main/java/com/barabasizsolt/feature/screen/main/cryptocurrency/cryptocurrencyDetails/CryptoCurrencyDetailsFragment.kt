package com.barabasizsolt.feature.screen.main.cryptocurrency.cryptocurrencyDetails

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.barabasizsolt.feature.R
import com.barabasizsolt.feature.databinding.FragmentCryptoCurrencyDetailsBinding
import com.barabasizsolt.feature.screen.main.MainFragment
import com.barabasizsolt.feature.shared.navigation.BaseFragment
import com.barabasizsolt.feature.shared.utils.BundleArgumentDelegate
import com.barabasizsolt.feature.shared.utils.createSnackBar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CryptoCurrencyDetailsFragment : BaseFragment<FragmentCryptoCurrencyDetailsBinding>(R.layout.fragment_crypto_currency_details) {
    private val viewModel: CryptoCurrencyDetailsViewModel by viewModel { parametersOf(arguments?.cryptoCurrencyId) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.setVariable(BR.viewModel, viewModel)
        (parentFragment as MainFragment).setAppBarTitle(title = view.context.getString(R.string.detail))
        val detailsAdapter = CryptoCurrencyDetailsAdapter(
            onChipClicked = viewModel::onChipClicked,
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
        is CryptoCurrencyDetailsViewModel.Event.ShowErrorMessage -> binding.root.createSnackBar(event.errorMessage) {
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
