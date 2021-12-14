package com.example.cryptoapp.feature.event

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.data.constant.ExchangeConstant
import com.example.cryptoapp.databinding.FragmentEventBinding
import com.example.cryptoapp.feature.shared.OnItemClickListener
import com.example.cryptoapp.feature.shared.OnItemLongClickListener
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventFragment : Fragment(), OnItemClickListener, OnItemLongClickListener {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var eventAdapter: EventAdapter
    private var isLoading: Boolean = true
    private var currentPage: Long = ExchangeConstant.PAGE.toLong()
    private var pastVisibleItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private lateinit var binding: FragmentEventBinding
    private val viewModel by viewModel<EventViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventBinding.inflate(inflater, container, false)
        initUI()
        return binding.root
    }

    private fun initUI() {
        linearLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.layoutManager = linearLayoutManager
        eventAdapter = EventAdapter(this, this)
        binding.recyclerview.adapter = eventAdapter
        viewModel.loadAllEvents(currentPage.toString())
        viewModel.events.onEach { response ->
            if (response != null && response.isSuccessful) {
                val events = response.body()?.data as MutableList
                Log.d("Exchanges", events.toString())
                if (currentPage.toString() == ExchangeConstant.PAGE) {
                    eventAdapter.submitList(events)
                } else {
                    eventAdapter.submitList(eventAdapter.currentList + events)
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
                            viewModel.loadAllEvents(currentPage.toString())
                            Log.d("End", currentPage.toString())
                        }
                    }
                }
            }
        })
    }

    override fun onItemClick(position: Int) {
        // TODO: implement it
    }

    override fun onItemLongClick(position: Int) {
        // TODO: implement it
    }
}
