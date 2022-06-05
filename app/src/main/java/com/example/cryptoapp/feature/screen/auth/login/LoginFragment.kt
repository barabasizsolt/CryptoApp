package com.example.cryptoapp.feature.screen.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.cryptoapp.R
import com.example.cryptoapp.feature.activity.MainActivity
import com.example.cryptoapp.feature.screen.auth.catalog.AuthButton
import com.example.cryptoapp.feature.screen.auth.catalog.EmailInput
import com.example.cryptoapp.feature.screen.auth.catalog.PasswordInput
import com.example.cryptoapp.feature.screen.auth.login.catalog.ForgotPasswordButton
import com.example.cryptoapp.feature.screen.auth.login.catalog.GoogleSingUpButton
import com.example.cryptoapp.feature.screen.auth.login.catalog.LoginScreenLogo
import com.example.cryptoapp.feature.screen.auth.login.catalog.SignUpButton
import com.example.cryptoapp.feature.screen.auth.register.SignUpFragment
import com.example.cryptoapp.feature.screen.main.watchlist.WatchListViewModel
import com.example.cryptoapp.feature.shared.catalog.LoadingIndicator
import com.example.cryptoapp.feature.shared.utils.ResetPasswordDialog
import com.example.cryptoapp.feature.shared.utils.createSnackBar
import com.example.cryptoapp.feature.shared.utils.handleReplace
import com.google.android.material.composethemeadapter.MdcTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private val viewModel by viewModel<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(context = requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MdcTheme {
                    LoginScreen(viewModel = viewModel)
                }
            }
        }
    }

    @Composable
    private fun LoginScreen(viewModel: LoginViewModel) {

        LoginScreenContent(viewModel = viewModel)

        when(val state = viewModel.screenState) {
            is LoginViewModel.ScreenState.Error ->
                LocalView.current.createSnackBar(message = state.message)
            is LoginViewModel.ScreenState.ShowResetPasswordDialog -> ResetPasswordDialog(
                onAccess = { viewModel.resetPassword() },
                onDismiss = { viewModel.onDismiss() },
                email = viewModel.resetPasswordEmail,
                onEmailChange = { viewModel.onResetPasswordEmailChange(email = it) }
            )
            else -> Unit
        }

        when(viewModel.action) {
            is LoginViewModel.Action.NavigateToHome ->
                (activity as MainActivity).navigateToMain()
            is LoginViewModel.Action.NavigateToRegister -> parentFragment?.childFragmentManager?.handleReplace(
                addToBackStack = true,
                newInstance = SignUpFragment.Companion::newInstance
            ).also { viewModel.reset() }
            else -> Unit
        }
    }

    @Composable
    private fun LoginScreenContent(viewModel: LoginViewModel) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(horizontal = dimensionResource(id = R.dimen.screen_padding)),
            modifier = Modifier
                .imePadding()
                .fillMaxSize()
        ) {
            item {
                LoginScreenLogo(
                    modifier = Modifier
                        .padding(
                            top = 150.dp,
                            bottom = dimensionResource(id = R.dimen.screen_padding)
                        )
                )
            }
            item {
                GoogleSingUpButton(
                    text = stringResource(id = R.string.continue_with_google),
                    onClick = {}
                )
            }
            item {
                EmailInput(
                    email = viewModel.email,
                    onEmailChange = viewModel::onEmailChange,
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.screen_padding))
                )
            }
            item {
                PasswordInput(
                    password = viewModel.password,
                    onPasswordChange = viewModel::onPasswordChange,
                    keyboardActions = {
                        viewModel.loginWithEmailAndPassword()
                    },
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.screen_padding) + dimensionResource(id = R.dimen.content_padding))
                )
            }
            item {
                AuthButton(
                    text = stringResource(id = R.string.login),
                    enabled = viewModel.isLoginEnabled.value,
                    isLoading = viewModel.screenState is LoginViewModel.ScreenState.Loading,
                    onClick = { viewModel.loginWithEmailAndPassword() }
                )
            }
            item {
                ForgotPasswordButton(
                    onClick = { viewModel.onResetPasswordClicked() }
                )
            }
            item { SignUpButton(onClick = { viewModel.onRegisterClicked() }) }
        }
    }
    
//    private fun listenToEvent(action: LoginViewModel.Action) = when (action) {
//        is LoginViewModel.Action.LoginUser -> navigator?.navigateToMain()
//        is LoginViewModel.Action.NavigateToRegister -> parentFragment?.childFragmentManager?.handleReplace(
//            addToBackStack = true,
//            newInstance = SignUpFragment.Companion::newInstance
//        )
//        is LoginViewModel.Action.ShowResetPasswordDialog -> showResetPasswordDialog()
//        is LoginViewModel.Action.ShowAfterResetPasswordMessage -> binding.root.createSnackBar(message = action.message)
//        is LoginViewModel.Action.ShowErrorMessage -> binding.root.createSnackBar(message = action.message)
//    }

//    private fun showResetPasswordDialog() {
//        viewModel.isOpen = true
//        binding.resetPasswordDialog.setContent {
//        }
//    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}
