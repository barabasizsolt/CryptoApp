package com.example.cryptoapp.feature.screen.auth.login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.cryptoapp.BR
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentLoginBinding
import com.example.cryptoapp.feature.screen.auth.register.SignUpFragment
import com.example.cryptoapp.feature.shared.navigation.BaseFragment
import com.example.cryptoapp.feature.shared.utils.ResetPasswordDialog
import com.example.cryptoapp.feature.shared.utils.createSnackBar
import com.example.cryptoapp.feature.shared.utils.handleReplace
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

// TODO[mid] refactor the xml
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val viewModel by viewModel<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.setVariable(BR.viewModel, viewModel)
        viewModel.event.onEach(::listenToEvent).launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun listenToEvent(event: LoginViewModel.Event) = when (event) {
        is LoginViewModel.Event.LoginUser -> navigator?.navigateToMain()
        is LoginViewModel.Event.NavigateToRegister -> parentFragment?.childFragmentManager?.handleReplace(
            addToBackStack = true,
            newInstance = SignUpFragment.Companion::newInstance
        )
        is LoginViewModel.Event.ShowResetPasswordDialog -> showResetPasswordDialog()
        is LoginViewModel.Event.ShowAfterResetPasswordMessage -> binding.root.createSnackBar(message = event.message)
        is LoginViewModel.Event.ShowErrorMessage -> binding.root.createSnackBar(message = event.message)
    }

    private fun showResetPasswordDialog() {
        viewModel.isOpen = true
        binding.resetPasswordDialog.setContent {
            ResetPasswordDialog(
                onAccess = { viewModel.resetPassword() },
                onDismiss = { viewModel.isOpen = false },
                isDismissed = !viewModel.isOpen,
                email = viewModel.resetPasswordEmail,
                onEmailChange = { viewModel.onEmailChange(email = it) }
            )
        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}
