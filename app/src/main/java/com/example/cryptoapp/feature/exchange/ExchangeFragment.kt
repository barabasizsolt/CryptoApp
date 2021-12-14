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
import com.example.cryptoapp.R
import com.example.cryptoapp.data.constant.ExchangeConstant.PAGE
import com.example.cryptoapp.data.constant.ExchangeConstant.PER_PAGE
import com.example.cryptoapp.data.model.exchange.Exchange
import com.example.cryptoapp.feature.shared.OnItemClickListener
import com.example.cryptoapp.feature.shared.OnItemLongClickListener
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExchangeFragment : Fragment(), OnItemClickListener, OnItemLongClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var exchangeAdapter: ExchangeAdapter

    private var isLoading: Boolean = true
    private var currentPage: Long = PAGE.toLong()
    private var pastVisibleItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0

    private val exchangeViewModel by viewModel<ExchangeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exchange, container, false)
        recyclerView = view.findViewById(R.id.recyclerview)
        initUI()
        return view
    }

    private fun initUI() {
        linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager
        exchangeAdapter = ExchangeAdapter(this, this)
        recyclerView.adapter = exchangeAdapter
        exchangeViewModel.loadExchanges()
        exchangeViewModel.exchanges.onEach { response ->
            if (response != null && response.isSuccessful) {
                Log.d("Exchanges", response.body()?.size.toString())
                val exchanges = response.body() as MutableList<Exchange>
                if (currentPage.toString() == PAGE) {
                    exchangeAdapter.submitList(exchanges)
                } else {
                    exchangeAdapter.submitList(exchangeAdapter.currentList + exchanges)
                    isLoading = true
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = linearLayoutManager.childCount
                    totalItemCount = linearLayoutManager.itemCount
                    pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition()
                    if (isLoading) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            isLoading = false
                            currentPage++
                            exchangeViewModel.loadExchanges(perPage = PER_PAGE, currentPage.toString())
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
