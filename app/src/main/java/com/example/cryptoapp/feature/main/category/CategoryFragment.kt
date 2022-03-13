package com.example.cryptoapp.feature.main.category

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoapp.BR
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentCategoryBinding
import com.example.cryptoapp.feature.shared.navigation.BaseFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryFragment : BaseFragment<FragmentCategoryBinding>(R.layout.fragment_category) {

    private val viewModel by viewModel<CategoryViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.setVariable(BR.viewModel, viewModel)
        val categoryAdapter = CategoryAdapter(
            onCategoryItemClick = {},
            onTryAgainButtonClicked = {}
        )
        binding.recyclerview.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = categoryAdapter
        }
        viewModel.listItem.onEach(categoryAdapter::submitList).launchIn(viewLifecycleOwner.lifecycleScope)
    }

    companion object {
        fun newInstance() = CategoryFragment()
    }
}