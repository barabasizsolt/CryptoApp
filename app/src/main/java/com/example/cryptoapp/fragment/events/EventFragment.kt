package com.example.cryptoapp.fragment.events

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.adapter.events.EventAdapter
import com.example.cryptoapp.api.coingekko.CoinGekkoApiRepository
import com.example.cryptoapp.api.coingekko.CoinGekkoApiViewModel
import com.example.cryptoapp.constant.coingekko.ExchangeConstant
import com.example.cryptoapp.interfaces.OnItemClickListener
import com.example.cryptoapp.interfaces.OnItemLongClickListener
import com.example.cryptoapp.model.events.AllEvents
import com.example.cryptoapp.model.events.Event
import retrofit2.Response


class EventFragment : Fragment(), OnItemClickListener, OnItemLongClickListener {
    private lateinit var viewModel: CoinGekkoApiViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var eventAdapter: EventAdapter

    private var isLoading : Boolean = true
    private var currentPage : Long = ExchangeConstant.PAGE.toLong()
    private var pastVisibleItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)

        bindUI(view)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.allEventsResponse.removeObserver(eventsObserver)
    }

    private fun bindUI(view: View){
        recyclerView = view.findViewById(R.id.recyclerview)
        viewModel = CoinGekkoApiViewModel(CoinGekkoApiRepository())
        viewModel.getAllEvents(currentPage.toString())
        viewModel.allEventsResponse.observe(requireActivity(), eventsObserver)
    }

    private fun initUI(events: MutableList<Event>){
        linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager
        eventAdapter = EventAdapter(events, this, this)
        recyclerView.adapter = eventAdapter

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
        if(response.isSuccessful){
            Log.d("Exchanges", response.body().toString())
            val events = response.body()?.data as MutableList<Event>
            if(currentPage.toString() == ExchangeConstant.PAGE) {
                initUI(events)
            }
            else{
                eventAdapter.addData(events)
                isLoading = true
            }
        }
    }

    override fun onItemClick(position: Int) {
        //TODO: implement it
    }

    override fun onItemLongClick(position: Int) {
        //TODO: implement it
    }
}