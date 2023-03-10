package com.barabasizsolt.feature.screen.auth.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import com.barabasizsolt.feature.R
import com.barabasizsolt.feature.activity.MainActivity
import com.barabasizsolt.feature.screen.auth.catalog.EmailInput
import com.barabasizsolt.feature.screen.auth.catalog.ForgotPasswordButton
import com.barabasizsolt.feature.screen.auth.catalog.GoogleSingUpButton
import com.barabasizsolt.feature.screen.auth.catalog.PasswordInput
import com.barabasizsolt.feature.screen.auth.catalog.ScreenLogo
import com.barabasizsolt.feature.screen.auth.catalog.SecondaryAuthButton
import com.barabasizsolt.feature.screen.auth.register.SignUpFragment
import com.barabasizsolt.feature.shared.catalog.CryptoAppButton
import com.barabasizsolt.feature.shared.catalog.ResetPasswordDialog
import com.barabasizsolt.feature.shared.utils.createSnackBar
import com.barabasizsolt.feature.shared.utils.handleReplace
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
            is LoginViewModel.Action.NavigateToHome -> (requireActivity() as MainActivity).navigateToMain()
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
                ScreenLogo(
                    modifier = Modifier
                        .padding(
                            top = 150.dp,
                            bottom = dimensionResource(id = R.dimen.screen_padding)
                        ),
                    isLoading = viewModel.screenState is LoginViewModel.ScreenState.Loading
                )
            }
            item {
                GoogleSingUpButton(
                    text = stringResource(id = R.string.continue_with_google),
                    onClick = {
                        val intent = viewModel.getIntentForGoogleLogin()
                        loginWithGoogleAccountLauncher.launch(intent)
                    }
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
                CryptoAppButton(
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
            item {
                SecondaryAuthButton(
                    question = stringResource(id = R.string.dont_have_account),
                    text = stringResource(id = R.string.sign_up),
                    onClick = { viewModel.onRegisterClicked() }
                )
            }
        }
    }

    private val loginWithGoogleAccountLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data = result.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            viewModel.loginWithGoogle(intent = data)
        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}
