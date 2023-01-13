package com.barabasizsolt.feature.screen.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.barabasizsolt.feature.screen.auth.catalog.PasswordInput
import com.barabasizsolt.feature.screen.auth.catalog.ScreenLogo
import com.barabasizsolt.feature.screen.auth.catalog.SecondaryAuthButton
import com.barabasizsolt.feature.shared.catalog.CryptoAppButton
import com.barabasizsolt.feature.shared.utils.createSnackBar
import com.google.android.material.composethemeadapter.MdcTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFragment : Fragment() {

    private val viewModel by viewModel<SignUpViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(context = requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MdcTheme {
                    SignUScreen(viewModel = viewModel)
                }
            }
        }
    }

    @Composable
    private fun SignUScreen(viewModel: SignUpViewModel) {

        SignUpScreenContent(viewModel = viewModel)

        when(val state = viewModel.screenState) {
            is SignUpViewModel.ScreenState.Error ->
                LocalView.current.createSnackBar(message = state.message)
            else -> Unit
        }

        when(viewModel.action) {
            is SignUpViewModel.Action.NavigateToHome -> (requireActivity() as MainActivity).navigateToMain()
            is SignUpViewModel.Action.NavigateToLogin -> viewModel.reset().also {
                parentFragment?.childFragmentManager?.popBackStack()
            }
            else -> Unit
        }
    }

    @Composable
    private fun SignUpScreenContent(viewModel: SignUpViewModel) {
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
                    isLoading = viewModel.screenState is SignUpViewModel.ScreenState.Loading
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
                        viewModel.registerWithEmailAndPassword()
                    },
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.screen_padding) + dimensionResource(id = R.dimen.content_padding))
                )
            }
            item {
                CryptoAppButton(
                    text = stringResource(id = R.string.sign_up),
                    enabled = viewModel.isRegisterEnabled.value,
                    isLoading = viewModel.screenState is SignUpViewModel.ScreenState.Loading,
                    onClick = { viewModel.registerWithEmailAndPassword() },
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.screen_padding))
                )
            }
            item {
                SecondaryAuthButton(
                    question = stringResource(id = R.string.already_have_an_account),
                    text = stringResource(id = R.string.sign_in),
                    onClick = { viewModel.onSingInClicked() }
                )
            }
        }
    }

    companion object {
        fun newInstance() = SignUpFragment()
    }
}
