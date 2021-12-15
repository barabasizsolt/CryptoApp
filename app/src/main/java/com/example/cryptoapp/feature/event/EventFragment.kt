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
    private val eventAdapter: EventAdapter = EventAdapter(this, this)
    private var currentPage: Long = ExchangeConstant.DEFAULT_PAGE.toLong()
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
        binding.recyclerview.adapter = eventAdapter
        viewModel.events.onEach { events ->
            Log.d("Events", events.toString())
            eventAdapter.submitList(events)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    currentPage++
                    viewModel.loadAllEvents(page = currentPage.toString())
                    Log.d("End", currentPage.toString())
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
