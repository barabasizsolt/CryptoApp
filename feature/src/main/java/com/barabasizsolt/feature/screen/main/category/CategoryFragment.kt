package com.barabasizsolt.feature.screen.main.category

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.barabasizsolt.feature.R
import com.barabasizsolt.feature.databinding.FragmentCategoryBinding
import com.barabasizsolt.feature.shared.navigation.BaseFragment
import com.barabasizsolt.feature.shared.utils.createSnackBar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryFragment : BaseFragment<FragmentCategoryBinding>(R.layout.fragment_category) {

    private val viewModel by viewModel<CategoryViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.setVariable(BR.viewModel, viewModel)
        val categoryAdapter = CategoryAdapter(
            onCategoryItemClick = {},
            onTryAgainButtonClicked = { viewModel.refreshData(isForceRefresh = true) }
        )
        binding.recyclerview.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = categoryAdapter
        }
        viewModel.listItem.onEach(categoryAdapter::submitList).launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.event.onEach(::listenToEvents).launchIn(viewLifecycleOwner.lifecycleScope)
        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.refreshData(isForceRefresh = true) }
    }

    private fun listenToEvents(event: CategoryViewModel.Event) = when (event) {
        is CategoryViewModel.Event.ShowErrorMessage -> binding.root.createSnackBar(event.errorMessage) {
            viewModel.refreshData(isForceRefresh = true)
        }
    }

    companion object {
        fun newInstance() = CategoryFragment()
    }
}
