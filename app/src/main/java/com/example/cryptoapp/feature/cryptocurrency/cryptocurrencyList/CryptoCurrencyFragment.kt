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
import com.example.cryptoapp.domain.cryptocurrency.Constant.tags
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
        viewModel.event.onEach(::listenToEvents).launchIn(viewLifecycleOwner.lifecycleScope)
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

    private fun listenToEvents(event: CryptoCurrencyViewModel.Event) = when (event) {
        is CryptoCurrencyViewModel.Event.DialogEvent -> createDialog(event)
        is CryptoCurrencyViewModel.Event.OpenDetailsPageEvent -> openDetailsPage(event)
    }

    private fun openDetailsPage(openDetailsPageEvent: CryptoCurrencyViewModel.Event.OpenDetailsPageEvent) {
        val fragment = CryptoCurrencyDetailsFragment()
        val bundle = Bundle()
        bundle.putString(COIN_ID, openDetailsPageEvent.id)
        fragment.arguments = bundle
        (activity as MainActivity).replaceFragment(
            fragment,
            R.id.activity_fragment_container,
            addToBackStack = true
        )
    }

    private fun createDialog(dialogEvent: CryptoCurrencyViewModel.Event.DialogEvent) {
        val checkedItems = BooleanArray(tags.size) { false }
        val selectedTags: MutableList<String> = mutableListOf()
        val elements = dialogEvent.dialogElements.toTypedArray()
        val dialogType = dialogEvent.dialogType
        val lastSelectedElement = dialogEvent.lastSelectedItemIndex
        val previouslySelectedTags = dialogEvent.selectedItems
        val chipType = dialogEvent.filterType
        var checkedItemIndex = lastSelectedElement

        previouslySelectedTags.forEach { tag ->
            checkedItems[tags.indexOf(tag)] = true
        }

        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(
                when (chipType) {
                    FilterChip.TAG_CHIP -> resources.getString(R.string.tags_title)
                    FilterChip.SORTING_CHIP -> resources.getString(R.string.sorting_title)
                    FilterChip.TIME_PERIOD_CHIP -> resources.getString(R.string.time_period_title)
                }
            )
            .setNeutralButton(resources.getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                when (dialogType) {
                    DialogType.SINGLE_CHOICE -> viewModel.onDialogItemSelected(filterChip = chipType, selectedItemIndex = checkedItemIndex)
                    DialogType.MULTI_CHOICE -> {
                        for (i in checkedItems.indices) {
                            if (checkedItems[i]) {
                                selectedTags.add(tags[i])
                            }
                        }
                        viewModel.onDialogItemSelected(filterChip = chipType, selectedItems = selectedTags)
                    }
                }
            }
        when (dialogType) {
            DialogType.SINGLE_CHOICE -> dialogBuilder.setSingleChoiceItems(
                elements, lastSelectedElement
            ) { _, which ->
                checkedItemIndex = which
            }
            DialogType.MULTI_CHOICE -> dialogBuilder.setMultiChoiceItems(
                elements, checkedItems
            ) { _, which, checked ->
                checkedItems[which] = checked
            }
        }
        dialogBuilder.show()
    }
}
