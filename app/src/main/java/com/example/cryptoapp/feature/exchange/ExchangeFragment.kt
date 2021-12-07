package com.example.cryptoapp.feature.exchange

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.data.repository.CoinGekkoApiRepository
import com.example.cryptoapp.feature.viewModel.CoinGekkoApiViewModel
import com.example.cryptoapp.data.repository.Cache
import com.example.cryptoapp.data.constant.ExchangeConstant.PAGE
import com.example.cryptoapp.data.constant.ExchangeConstant.PER_PAGE
import com.example.cryptoapp.feature.shared.OnItemClickListener
import com.example.cryptoapp.feature.shared.OnItemLongClickListener
import com.example.cryptoapp.data.model.exchange.Exchange
import retrofit2.Response

class ExchangeFragment : Fragment(), OnItemClickListener, OnItemLongClickListener {
    private lateinit var viewModel: CoinGekkoApiViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var exchangeAdapter: ExchangeAdapter

    private var isLoading: Boolean = true
    private var currentPage: Long = PAGE.toLong()
    private var pastVisibleItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exchange, container, false)
        bindUI(view)
        initUI()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.allExchangeResponse.removeObserver(exchangesObserver)
    }

    private fun bindUI(view: View) {
        recyclerView = view.findViewById(R.id.recyclerview)
        viewModel = CoinGekkoApiViewModel(CoinGekkoApiRepository())
        viewModel.getAllExchanges()
        viewModel.allExchangeResponse.observe(requireActivity(), exchangesObserver)
    }

    private fun initUI() {
        linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager
        exchangeAdapter = ExchangeAdapter(this, this)
        recyclerView.adapter = exchangeAdapter
        viewModel.getAllExchanges(perPage = PER_PAGE, currentPage.toString())

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
                            viewModel.getAllExchanges(perPage = PER_PAGE, currentPage.toString())
                            Log.d("End", currentPage.toString())
                        }
                    }
                }
            }
        })
    }

    private val exchangesObserver =
        androidx.lifecycle.Observer<Response<List<Exchange>>> { response ->
            if (response.isSuccessful) {
                Log.d("Exchanges", response.body().toString())
                val exchanges = response.body() as MutableList<Exchange>
                if (currentPage.toString() == PAGE) {
                    Cache.setExchanges(exchanges)
                } else {
                    Cache.setExchanges(
                        Cache.getExchanges().plus(exchanges) as MutableList<Exchange>
                    )
                    isLoading = true
                }
                exchangeAdapter.submitList(Cache.getExchanges())
            }
        }

    override fun onItemClick(position: Int) {
        //TODO:implement it
    }

    override fun onItemLongClick(position: Int) {
        //TODO:implement it
    }
}