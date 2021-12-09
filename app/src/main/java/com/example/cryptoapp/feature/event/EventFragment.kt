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
import com.example.cryptoapp.data.model.event.Event
import com.example.cryptoapp.data.repository.CoinGekkoApiRepository
import com.example.cryptoapp.feature.shared.OnItemClickListener
import com.example.cryptoapp.feature.shared.OnItemLongClickListener
import com.example.cryptoapp.feature.viewModel.CoinGekkoApiViewModel
import retrofit2.Response

class EventFragment : Fragment(), OnItemClickListener, OnItemLongClickListener {
    private lateinit var viewModel: CoinGekkoApiViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var eventAdapter: EventAdapter

    private var isLoading: Boolean = true
    private var currentPage: Long = ExchangeConstant.PAGE.toLong()
    private var pastVisibleItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0

    private val events = mutableListOf<Event>()

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

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.allEventsResponse.removeObserver(eventsObserver)
    }

    private fun bindUI(view: View) {
        recyclerView = view.findViewById(R.id.recyclerview)
        viewModel = CoinGekkoApiViewModel(CoinGekkoApiRepository())
        viewModel.getAllEvents(currentPage.toString())
        viewModel.allEventsResponse.observe(requireActivity(), eventsObserver)
    }

    private fun initUI() {
        linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager
        eventAdapter = EventAdapter(this, this)
        recyclerView.adapter = eventAdapter
        viewModel.getAllEvents(currentPage.toString())

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
                            viewModel.getAllEvents(currentPage.toString())
                            Log.d("End", currentPage.toString())
                        }
                    }
                }
            }
        })
    }

    private val eventsObserver = androidx.lifecycle.Observer<Response<AllEvents>> { response ->
        if (response.isSuccessful) {
            Log.d("Exchanges", response.body().toString())
            if (currentPage.toString() == ExchangeConstant.PAGE) {
                events.clear()
            } else {
                isLoading = true
            }
            events.addAll(response.body()?.data as MutableList<Event>)
            eventAdapter.submitList(events)
        }
    }

    override fun onItemClick(position: Int) {
        // TODO: implement it
    }

    override fun onItemLongClick(position: Int) {
        // TODO: implement it
    }
}
