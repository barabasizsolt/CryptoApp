package com.example.cryptoapp.feature.auth.login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.cryptoapp.BR
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentLoginBinding
import com.example.cryptoapp.feature.auth.signup.SignUpFragment
import com.example.cryptoapp.feature.shared.navigation.BaseFragment
import com.example.cryptoapp.feature.shared.utils.createErrorSnackBar
import com.example.cryptoapp.feature.shared.utils.handleReplace
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val viewModel by viewModel<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.setVariable(BR.viewModel, viewModel)
        viewModel.event.onEach(::listenToEvent).launchIn(viewLifecycleOwner.lifecycleScope)
        binding.signUp.setOnClickListener {
            parentFragment?.childFragmentManager?.handleReplace(
                addToBackStack = true,
                newInstance = SignUpFragment.Companion::newInstance
            )
        }
    }

    private fun listenToEvent(event: LoginViewModel.Event) = when (event) {
        is LoginViewModel.Event.LoginUser -> navigator?.navigateToMain()
        is LoginViewModel.Event.ShowErrorMessage -> binding.root.createErrorSnackBar(errorMessage = event.message)
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}
