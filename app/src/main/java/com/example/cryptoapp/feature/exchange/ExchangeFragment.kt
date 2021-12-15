package com.example.cryptoapp.feature.exchange

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.data.constant.ExchangeConstant.PAGE
import com.example.cryptoapp.data.constant.ExchangeConstant.PER_PAGE
import com.example.cryptoapp.databinding.FragmentExchangeBinding
import com.example.cryptoapp.feature.shared.OnItemClickListener
import com.example.cryptoapp.feature.shared.OnItemLongClickListener
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExchangeFragment : Fragment(), OnItemClickListener, OnItemLongClickListener {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val exchangeAdapter: ExchangeAdapter = ExchangeAdapter(this, this)
    private var isLoading: Boolean = true
    private var currentPage: Long = PAGE.toLong()
    private var pastVisibleItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private lateinit var binding: FragmentExchangeBinding
    private val viewModel by viewModel<ExchangeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExchangeBinding.inflate(inflater, container, false)
        initUI()
        return binding.root
    }

    private fun initUI() {
        linearLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.layoutManager = linearLayoutManager
        binding.recyclerview.adapter = exchangeAdapter
        viewModel.exchanges.onEach { exchanges ->
            if (exchanges != null) {
                Log.d("Exchanges", exchanges.size.toString())
                if (currentPage.toString() == PAGE) {
                    exchangeAdapter.submitList(exchanges)
                } else {
                    exchangeAdapter.submitList(exchangeAdapter.currentList + exchanges)
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
                            currentPage++
                            viewModel.loadExchanges(perPage = PER_PAGE, currentPage.toString())
                            Log.d("End", currentPage.toString())
                        }
                    }
                }
            }
        })
    }

    override fun onItemClick(position: Int) {
        // TODO:implement it
    }

    override fun onItemLongClick(position: Int) {
        // TODO:implement it
    }
}
