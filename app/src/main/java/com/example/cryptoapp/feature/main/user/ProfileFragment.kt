package com.example.cryptoapp.feature.main.user

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoapp.BR
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentProfileBinding
import com.example.cryptoapp.feature.shared.navigation.BaseFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private val viewModel by viewModel<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.setVariable(BR.viewModel, viewModel)
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
    }

    private fun signOutAfterConfirmation() = MaterialAlertDialogBuilder(requireContext())
        .setTitle(R.string.general_close_confirmation_title)
        .setMessage(R.string.general_sign_out_confirmation_message)
        .setPositiveButton(R.string.general_close_confirmation_positive) { _, _ ->
            FirebaseAuth.getInstance().signOut()
            navigator?.navigateToAuthentication()
        }
        .setNegativeButton(R.string.general_close_confirmation_negative, null)
        .show()

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
