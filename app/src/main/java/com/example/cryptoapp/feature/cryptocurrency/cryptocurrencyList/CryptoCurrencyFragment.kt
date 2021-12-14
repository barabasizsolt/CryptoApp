package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.data.constant.CryptoConstant
import com.example.cryptoapp.data.constant.CryptoConstant.CHECKED_SORTING_ITEM_INDEX
import com.example.cryptoapp.data.constant.CryptoConstant.CHECKED_TIME_PERIOD_ITEM_INDEX
import com.example.cryptoapp.data.constant.CryptoConstant.LIMIT
import com.example.cryptoapp.data.constant.CryptoConstant.OFFSET
import com.example.cryptoapp.data.constant.CryptoConstant.filterTags
import com.example.cryptoapp.data.constant.CryptoConstant.sortingParams
import com.example.cryptoapp.data.constant.CryptoConstant.sortingTags
import com.example.cryptoapp.data.constant.CryptoConstant.timePeriods
import com.example.cryptoapp.data.constant.CryptoConstant.toCryptoCurrencyUIModel
import com.example.cryptoapp.databinding.FragmentCryptoCurrencyBinding
import com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails.CryptoCurrencyDetailsFragment
import com.example.cryptoapp.feature.shared.OnItemClickListener
import com.example.cryptoapp.feature.shared.OnItemLongClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class CryptoCurrencyFragment : Fragment(), OnItemClickListener, OnItemLongClickListener {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var cryptoCurrencyAdapter: CryptoCurrencyAdapter
    private var isLoading: Boolean = true
    private var pastVisibleItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var checkedSortingItemIndex = CHECKED_SORTING_ITEM_INDEX
    private var sortingParam: Pair<String, String> = sortingParams[checkedSortingItemIndex]
    private var checkedTimePeriodItemIndex = CHECKED_TIME_PERIOD_ITEM_INDEX
    private var timePeriod = timePeriods[checkedTimePeriodItemIndex]
    private var currentOffset = OFFSET
    private val tags: MutableSet<String> = mutableSetOf()
    private lateinit var binding: FragmentCryptoCurrencyBinding
    private val viewModel by viewModel<CryptoCurrencyViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCryptoCurrencyBinding.inflate(inflater, container, false)
        initUI()
        initChipTags()
        initTimePeriod()
        initChipSortBy()
        return binding.root
    }

    override fun onItemClick(position: Int) {
        val currentCryptoCurrency = cryptoCurrencyAdapter.currentList[position]
        val fragment = CryptoCurrencyDetailsFragment()
        val bundle = Bundle()
        bundle.putString(CryptoConstant.COIN_ID, currentCryptoCurrency.uuid)
        fragment.arguments = bundle
        (activity as MainActivity).replaceFragment(
            fragment,
            R.id.activity_fragment_container,
            addToBackStack = true
        )
    }

    override fun onItemLongClick(position: Int) {
        Log.d("OnLongClick", "Long Click")
    }

    private fun initUI() {
        (activity as MainActivity).bottomNavigationView.visibility = View.VISIBLE
        (activity as MainActivity).topAppBar.visibility = View.VISIBLE

        linearLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.layoutManager = linearLayoutManager
        cryptoCurrencyAdapter = CryptoCurrencyAdapter(this, this)
        binding.recyclerview.adapter = cryptoCurrencyAdapter
        viewModel.loadCryptoCurrencies()
        viewModel.cryptoCurrencies
            .onEach { response ->
                if (response != null && response.isSuccessful) {
                    Log.d("Observed", response.body()?.data?.coins?.size.toString())

                    val currentCryptoCurrencies = response.body()?.data?.coins?.map { currency ->
                        currency.toCryptoCurrencyUIModel(timePeriod)
                    } as MutableList

                    if (currentOffset == OFFSET) {
                        cryptoCurrencyAdapter.submitList(currentCryptoCurrencies)
                    } else {
                        cryptoCurrencyAdapter.submitList(cryptoCurrencyAdapter.currentList + currentCryptoCurrencies)
                        isLoading = true
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = linearLayoutManager.childCount
                    totalItemCount = linearLayoutManager.itemCount
                    pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition()
                    if (isLoading) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            isLoading = false
                            currentOffset += LIMIT
                            viewModel.loadCryptoCurrencies(
                                sortingParam.first,
                                sortingParam.second,
                                currentOffset
                            )
                            Log.v("End", currentOffset.toString())
                        }
                    }
                }
            }
        })
    }

    private fun initChipTags() {
        binding.chipTags.setOnClickListener {
            val checkedItems = BooleanArray(filterTags.size) { false }
            tags.forEach { tag ->
                val index = filterTags.indexOf(tag)
                checkedItems[index] = true
            }

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.tags_title))
                .setNeutralButton(resources.getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                    tags.clear()
                    for (i in checkedItems.indices) {
                        if (checkedItems[i]) {
                            tags.add(filterTags[i])
                        }
                    }
                    Log.d("checkedItems", tags.toString())
                    currentOffset = OFFSET
                    viewModel.loadCryptoCurrencies(
                        sortingParam.first,
                        sortingParam.second,
                        OFFSET,
                        tags,
                        timePeriod
                    )
                }
                .setMultiChoiceItems(filterTags, checkedItems) { _, which, checked ->

                    checkedItems[which] = checked
                }
                .show()
        }
    }

    private fun initTimePeriod() {
        binding.chipTimePeriod.setOnClickListener {
            timePeriod = timePeriods[checkedTimePeriodItemIndex]

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.time_period_title))
                .setNeutralButton(resources.getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                    currentOffset = OFFSET
                    viewModel.loadCryptoCurrencies(
                        sortingParam.first,
                        sortingParam.second,
                        OFFSET,
                        tags,
                        timePeriod
                    )
                }
                .setSingleChoiceItems(timePeriods, checkedTimePeriodItemIndex) { _, which ->
                    timePeriod = timePeriods[which]
                    checkedTimePeriodItemIndex = which
                }
                .show()
        }
    }

    private fun initChipSortBy() {
        binding.chipSortBy.setOnClickListener {
            sortingParam = sortingParams[checkedSortingItemIndex]

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.sorting_title))
                .setNeutralButton(resources.getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                    currentOffset = OFFSET
                    viewModel.loadCryptoCurrencies(
                        sortingParam.first,
                        sortingParam.second,
                        OFFSET,
                        tags,
                        timePeriod
                    )
                }
                .setSingleChoiceItems(sortingTags, checkedSortingItemIndex) { _, which ->
                    sortingParam = sortingParams[which]
                    checkedSortingItemIndex = which
                }
                .show()
        }
    }
}
