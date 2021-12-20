package com.example.cryptoapp.feature.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.data.constant.NewsConstant
import com.example.cryptoapp.databinding.FragmentNewsBinding
import com.example.cryptoapp.feature.shared.OnItemClickListener
import com.example.cryptoapp.feature.shared.OnItemLongClickListener
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : Fragment(), OnItemClickListener, OnItemLongClickListener {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val newsAdapter: NewsAdapter = NewsAdapter(this, this)
    private var currentPage: Long = NewsConstant.DEFAULT_PAGE.toLong()
    private lateinit var binding: FragmentNewsBinding
    private val viewModel by viewModel<NewsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        initUI()
        return binding.root
    }

    private fun initUI() {
        linearLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.layoutManager = linearLayoutManager
        binding.recyclerview.adapter = newsAdapter
        viewModel.news.onEach { events ->
            Log.d("News", events.toString())
            newsAdapter.submitList(events)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    currentPage++
                    viewModel.loadAllNews(page = currentPage.toString())
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
