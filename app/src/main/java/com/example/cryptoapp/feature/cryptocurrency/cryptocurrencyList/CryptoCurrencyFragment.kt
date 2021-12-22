package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentCryptoCurrencyBinding
import com.example.cryptoapp.domain.cryptocurrency.Constant.COIN_ID
import com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails.CryptoCurrencyDetailsFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class CryptoCurrencyFragment : Fragment() {
    private lateinit var binding: FragmentCryptoCurrencyBinding
    private val viewModel by viewModel<CryptoCurrencyViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCryptoCurrencyBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    private fun openDetailsPage(event: CryptoCurrencyViewModel.Event) {
        val id = (event as CryptoCurrencyViewModel.Event.OpenDetailsPageEvent).id
        val fragment = CryptoCurrencyDetailsFragment()
        val bundle = Bundle()
        bundle.putString(COIN_ID, id)
        fragment.arguments = bundle
        (activity as MainActivity).replaceFragment(
            fragment,
            R.id.activity_fragment_container,
            addToBackStack = true
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).bottomNavigationView.visibility = View.VISIBLE
        (activity as MainActivity).topAppBar.visibility = View.VISIBLE

        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        val cryptoCurrencyAdapter = CryptoCurrencyAdapter(
            onCryptoCurrencyItemClicked = viewModel::onCryptoCurrencyItemClicked,
            onLoadMoreCryptoCurrency = { viewModel.refreshData(isForceRefresh = false) },
            onTryAgainButtonClicked = { viewModel.refreshData(isForceRefresh = true) }
        )
        binding.recyclerview.adapter = cryptoCurrencyAdapter
        viewModel.listItems.onEach(cryptoCurrencyAdapter::submitList).launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.dialogEvent.onEach(::createDialog).launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.event.onEach(::openDetailsPage).launchIn(viewLifecycleOwner.lifecycleScope)
        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.refreshData(isForceRefresh = true) }
        binding.chipTimePeriod.setOnClickListener {
            viewModel.onChipClicked(filterChip = FilterChip.TIME_PERIOD_CHIP)
        }
        binding.chipSortBy.setOnClickListener {
            viewModel.onChipClicked(filterChip = FilterChip.SORTING_CHIP)
        }
        binding.chipTags.setOnClickListener {
            viewModel.onChipClicked(filterChip = FilterChip.TAG_CHIP)
        }
    }

    private fun createDialog(event: Pair<FilterChip, CryptoCurrencyViewModel.Event.DialogEvent>) {
        var checkedItem = 0
        val chipType = event.first
        val dialogEvent = event.second

        // TODO: based on ChipType change title, and make single/multi choice dialog
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.time_period_title))
            .setNeutralButton(resources.getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                viewModel.onDialogItemSelected(filterChip = chipType, selectedItemIndex = checkedItem)
            }
            .setSingleChoiceItems(dialogEvent.dialogElements.toTypedArray(), dialogEvent.lastSelectedItemIndex) { _, which ->
                checkedItem = which
            }
            .show()
    }
}
