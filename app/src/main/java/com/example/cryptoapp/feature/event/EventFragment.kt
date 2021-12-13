package com.example.cryptoapp.feature.event

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.data.constant.ExchangeConstant
import com.example.cryptoapp.data.model.event.AllEvents
import com.example.cryptoapp.feature.shared.OnItemClickListener
import com.example.cryptoapp.feature.shared.OnItemLongClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Response

class EventFragment : Fragment(), OnItemClickListener, OnItemLongClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var eventAdapter: EventAdapter

    private var isLoading: Boolean = true
    private var currentPage: Long = ExchangeConstant.PAGE.toLong()
    private var pastVisibleItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0

    private val eventViewModel by viewModel<EventViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)
        bindUI(view)
        initUI()
        return view
    }

    private fun bindUI(view: View) {
        recyclerView = view.findViewById(R.id.recyclerview)
    }

    private fun initUI() {
        linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager
        eventAdapter = EventAdapter(this, this)
        recyclerView.adapter = eventAdapter
        eventViewModel.loadAllEvents(currentPage.toString())
        eventViewModel.getEvents().observe(requireActivity(), eventsObserver)

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
                            eventViewModel.loadAllEvents(currentPage.toString())
                            Log.d("End", currentPage.toString())
                        }
                    }
                }
            }
        })
    }

    private val eventsObserver = androidx.lifecycle.Observer<Response<AllEvents>> { response ->
        if (response.isSuccessful) {
            val events = response.body()?.data as MutableList
            Log.d("Exchanges", events.toString())
            if (currentPage.toString() == ExchangeConstant.PAGE) {
                eventAdapter.submitList(events)
            } else {
                eventAdapter.submitList(eventAdapter.currentList + events)
                isLoading = true
            }
        }
    }

    override fun onItemClick(position: Int) {
        // TODO: implement it
    }

    override fun onItemLongClick(position: Int) {
        // TODO: implement it
    }
}
