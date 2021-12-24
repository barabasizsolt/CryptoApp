package com.example.cryptoapp.feature.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoapp.databinding.FragmentNewsBinding
import com.example.cryptoapp.feature.shared.createErrorSnackBar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding
    private val viewModel by viewModel<NewsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        val newsAdapter = NewsAdapter(
            onNewsItemClicked = viewModel::onNewsItemClicked,
            onLoadMoreBound = { viewModel.refreshData(isForceRefresh = false) },
            onTryAgainButtonClicked = { viewModel.refreshData(isForceRefresh = true) }
        )
        binding.recyclerview.adapter = newsAdapter
        viewModel.listItems.onEach(newsAdapter::submitList).launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.event.onEach(::listenToEvents).launchIn(viewLifecycleOwner.lifecycleScope)
        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.refreshData(isForceRefresh = true) }
    }

    private fun listenToEvents(event: NewsViewModel.Event) = when (event) {
        is NewsViewModel.Event.ShowErrorMessage -> binding.root.createErrorSnackBar(event.errorMessage) {
            viewModel.refreshData(isForceRefresh = true)
        }
        is NewsViewModel.Event.OpenBrowser -> openBrowser(event)
    }

    private fun openBrowser(event: NewsViewModel.Event.OpenBrowser) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(event.url)))
    }
}
