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
import com.example.cryptoapp.data.constant.ExchangeConstant.DEFAULT_PAGE
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
    private var currentPage: Long = DEFAULT_PAGE.toLong()
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
            Log.d("Exchanges", exchanges.size.toString())
            exchangeAdapter.submitList(exchanges)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    currentPage++
                    viewModel.loadExchanges(perPage = PER_PAGE, currentPage.toString())
                    Log.d("End", currentPage.toString())
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
