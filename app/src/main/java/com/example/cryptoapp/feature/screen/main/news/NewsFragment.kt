package com.example.cryptoapp.feature.screen.main.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoapp.BR
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentNewsBinding
import com.example.cryptoapp.feature.screen.main.MainFragment
import com.example.cryptoapp.feature.shared.navigation.BaseFragment
import com.example.cryptoapp.feature.shared.utils.createSnackBar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : BaseFragment<FragmentNewsBinding>(R.layout.fragment_news) {

    private val viewModel by viewModel<NewsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.setVariable(BR.viewModel, viewModel)
        (parentFragment as MainFragment).setAppBarTitle(title = view.context.getString(R.string.news))
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
        is NewsViewModel.Event.ShowErrorMessage -> binding.root.createSnackBar(event.errorMessage) {
            viewModel.refreshData(isForceRefresh = true)
        }
        is NewsViewModel.Event.OpenBrowser -> openBrowser(event)
    }

    private fun openBrowser(event: NewsViewModel.Event.OpenBrowser) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(event.url)))
    }

    companion object {
        fun newInstance() = NewsFragment()
    }
}
