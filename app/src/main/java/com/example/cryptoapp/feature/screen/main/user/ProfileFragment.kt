package com.example.cryptoapp.feature.screen.main.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.cryptoapp.R
import com.example.cryptoapp.feature.screen.auth.catalog.AuthButton
import com.example.cryptoapp.feature.screen.auth.catalog.EmailInput
import com.example.cryptoapp.feature.screen.auth.catalog.ForgotPasswordButton
import com.example.cryptoapp.feature.screen.auth.catalog.GoogleSingUpButton
import com.example.cryptoapp.feature.screen.auth.catalog.PasswordInput
import com.example.cryptoapp.feature.screen.auth.catalog.SecondaryAuthButton
import com.example.cryptoapp.feature.screen.auth.login.LoginViewModel
import com.example.cryptoapp.feature.screen.main.MainFragment
import com.example.cryptoapp.feature.screen.main.user.catalog.Header
import com.example.cryptoapp.feature.screen.main.user.catalog.ProfileIcon
import com.example.cryptoapp.feature.screen.main.user.catalog.ProfileTextItem
import com.example.cryptoapp.feature.screen.main.user.catalog.SignOutButton
import com.example.cryptoapp.feature.screen.main.user.catalog.UpdateProfileButton
import com.google.android.material.composethemeadapter.MdcTheme
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private val viewModel by viewModel<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(context = requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                (parentFragment as MainFragment).setAppBarTitle(title = LocalContext.current.getString(R.string.profile))
                MdcTheme {
                    if (viewModel.screenState == ProfileViewModel.ScreenState.Normal) {
                        ProfileScreenContent(viewModel = viewModel)
                    }
                }
            }
        }
    }

    @Composable
    private fun ProfileScreenContent(viewModel: ProfileViewModel) {
        viewModel.user?.let { user ->
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(all = dimensionResource(id = R.dimen.screen_padding)),
                modifier = Modifier
                    .imePadding()
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.screen_padding))
            ) {
                item { ProfileIcon(photoUrl = user.photoUrl) }
                item { Header(text = "General account details", modifier = Modifier.offset(y = 10.dp)) }
                item {
                    ProfileTextItem(
                        text = user.userName,
                        onTextChange = viewModel::onNameChange,
                        readOnly = false,
                        label = stringResource(id = R.string.user_name),
                        placeholder = stringResource(id = R.string.enter_user_name),
                        leadingIcon = Icons.Filled.Person
                    )
                }
                item {
                    ProfileTextItem(
                        text = user.email,
                        label = stringResource(id = R.string.email),
                        placeholder = stringResource(id = R.string.empty),
                        leadingIcon = Icons.Filled.Email
                    )
                }
                item {
                    ProfileTextItem(
                        text = "********************",
                        label = stringResource(id = R.string.password),
                        placeholder = stringResource(id = R.string.empty),
                        leadingIcon = Icons.Filled.Lock
                    )
                }
                item {
                    SignOutButton(isLoading = false) {

                    }
                }
                item { Header(text = "Other account details", modifier = Modifier.offset(y = 10.dp)) }
                item {
                    ProfileTextItem(
                        text = user.phoneNumber,
                        onTextChange = viewModel::onPhoneNumberChange,
                        readOnly = false,
                        label = stringResource(id = R.string.phone_number),
                        placeholder = stringResource(id = R.string.enter_phone_number),
                        leadingIcon = Icons.Filled.Phone
                    )
                }
                item {
                    ProfileTextItem(
                        text = user.registrationDate,
                        label = stringResource(id = R.string.registration_date),
                        placeholder = stringResource(id = R.string.empty),
                        leadingIcon = Icons.Filled.DateRange
                    )
                }
                item {
                    ProfileTextItem(
                        text = user.lastSignInDate,
                        label = stringResource(id = R.string.last_sign_in_date),
                        placeholder = stringResource(id = R.string.empty),
                        leadingIcon = Icons.Filled.DateRange
                    )
                }
                item {
                    ProfileTextItem(
                        text = user.isAnonymous,
                        label = stringResource(id = R.string.anonymous_account),
                        placeholder = stringResource(id = R.string.empty),
                        leadingIcon = Icons.Filled.Face
                    )
                }
                item {
                    UpdateProfileButton(isLoading = false) {

                    }
                }
            }
        }
    }

//    private fun listenToEvents(event: ProfileViewModel.Event) = when (event) {
//        is ProfileViewModel.Event.ShowErrorMessage -> binding.root.createSnackBar(event.message)
//        is ProfileViewModel.Event.SignOut -> navigator?.navigateToAuthentication()
//    }

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
