package com.example.cryptoapp.feature.screen.main.user

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoapp.BR
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentProfileBinding
import com.example.cryptoapp.feature.screen.main.MainFragment
import com.example.cryptoapp.feature.shared.navigation.BaseFragment
import com.example.cryptoapp.feature.shared.utils.createSnackBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private val viewModel by viewModel<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.setVariable(BR.viewModel, viewModel)
        (parentFragment as MainFragment).setAppBarTitle(title = view.context.getString(R.string.profile))
        val profileAdapter = ProfileAdapter(
            onChangePasswordClicked = {},
            onSignOutClicked = { signOutAfterConfirmation() },
            onTryAgainButtonClicked = {}
        )
        binding.recyclerview.let {
            it.adapter = profileAdapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.listItem.onEach(profileAdapter::submitList).launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.event.onEach(::listenToEvents).launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun listenToEvents(event: ProfileViewModel.Event) = when (event) {
        is ProfileViewModel.Event.ShowErrorMessage -> binding.root.createSnackBar(event.message)
        is ProfileViewModel.Event.SignOut -> navigator?.navigateToAuthentication()
    }

    private fun signOutAfterConfirmation() = MaterialAlertDialogBuilder(requireContext())
        .setTitle(R.string.general_close_confirmation_title)
        .setMessage(R.string.general_sign_out_confirmation_message)
        .setPositiveButton(R.string.general_close_confirmation_positive) { _, _ ->
            viewModel.logOutUser()
        }
        .setNegativeButton(R.string.general_close_confirmation_negative, null)
        .show()

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
