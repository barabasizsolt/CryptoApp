package com.example.cryptoapp.feature.screen.main.user

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.cryptoapp.R
import com.example.cryptoapp.feature.activity.MainActivity
import com.example.cryptoapp.feature.screen.main.MainFragment
import com.example.cryptoapp.feature.screen.main.user.catalog.BottomSheetContent
import com.example.cryptoapp.feature.screen.main.user.catalog.Header
import com.example.cryptoapp.feature.screen.main.user.catalog.ProfileIcon
import com.example.cryptoapp.feature.screen.main.user.catalog.ProfileTextItem
import com.example.cryptoapp.feature.shared.catalog.CryptoAppButton
import com.example.cryptoapp.feature.shared.catalog.LoadingIndicator
import com.example.cryptoapp.feature.shared.utils.createSnackBar
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.material.composethemeadapter.MdcTheme
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream


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
                    ProfileScreen(viewModel = viewModel)
                }
            }
        }
    }

    @Composable
    private fun ProfileScreen(viewModel: ProfileViewModel) {

        if (viewModel.user != null) ProfileScreenContent(viewModel = viewModel) else LoadingIndicator(isRefreshing = true)

        when (val state = viewModel.screenState) {
            is ProfileViewModel.ScreenState.Loading -> LoadingIndicator(isRefreshing = true)
            is ProfileViewModel.ScreenState.ShowSnackBar -> LocalView.current.createSnackBar(message = state.message)
            is ProfileViewModel.ScreenState.LogOutDialog -> signOutAfterConfirmation()
            else -> Unit
        }

        when (viewModel.action) {
            is ProfileViewModel.Action.NavigateToResetPassword -> {  }
            is ProfileViewModel.Action.NavigateToSignIn -> (activity as MainActivity).navigateToAuthentication()
            else -> Unit
        }
    }
    
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun ProfileScreenContent(viewModel: ProfileViewModel) {
        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
        )
        val coroutineScope = rememberCoroutineScope()

        BottomSheetScaffold(
            sheetContent = {
                BottomSheetContent(
                    modifier = Modifier
                        .height(height = 210.dp)
                        .offset(y = 10.dp),
                    onCancelClicked = {
                       coroutineScope.launch {
                           bottomSheetScaffoldState.bottomSheetState.collapse()
                       }
                    },
                    onTakePhotoClicked = {
                        openCameraLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                    },
                    onOpenGalleryClicked = {
                        openGalleryLauncher.launch(
                            Intent.createChooser(
                                Intent().apply {
                                    type = "image/*"
                                    action = Intent.ACTION_GET_CONTENT
                                },
                                "Select Picture"
                            )
                        )
                    }
                )
            },
            scaffoldState = bottomSheetScaffoldState,
            sheetPeekHeight = 0.dp,
            sheetShape = RoundedCornerShape(
                topStart = CornerSize(size = dimensionResource(id = R.dimen.screen_padding)),
                topEnd = CornerSize(size = dimensionResource(id = R.dimen.screen_padding)),
                bottomStart = CornerSize(size = 0.dp),
                bottomEnd = CornerSize(size = 0.dp)
            )
        ) {
            viewModel.user?.let { user ->
                SwipeRefresh(
                    state = rememberSwipeRefreshState(viewModel.screenState is ProfileViewModel.ScreenState.Loading),
                    onRefresh = viewModel::refreshData,
                ) {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        contentPadding = PaddingValues(
                            start = dimensionResource(id = R.dimen.screen_padding),
                            end = dimensionResource(id = R.dimen.screen_padding),
                            top = dimensionResource(id = R.dimen.screen_padding),
                            bottom = dimensionResource(id = R.dimen.screen_padding) + if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) 0.dp else 200.dp
                        ),
                        modifier = Modifier
                            .imePadding()
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.screen_padding))
                    ) {
                        item {
                            ProfileIcon(photoUrl = user.photo) {
                                coroutineScope.launch {
                                    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                        bottomSheetScaffoldState.bottomSheetState.expand()
                                    } else {
                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                    }
                                }
                            }
                        }
                        item {
                            Header(
                                text = stringResource(id = R.string.profile_info),
                                modifier = Modifier
                                    .offset(y = 10.dp)
                                    .fillMaxWidth()
                            )
                        }
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
                            CryptoAppButton(
                                text = stringResource(id = R.string.sign_out),
                                enabled = true,
                                isLoading = false
                            ) { viewModel.onLogOutClicked() }
                        }
                        item {
                            Header(
                                text = stringResource(id = R.string.account_info),
                                modifier = Modifier
                                    .offset(y = 10.dp)
                                    .fillMaxWidth()
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
                            CryptoAppButton(
                                text = stringResource(id = R.string.update_profile),
                                enabled = true,
                                isLoading = false
                            ) { viewModel.updateUser() }
                        }
                    }
                }
            }
        }
    }

    private val openGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data = result.data
        if (result.resultCode == RESULT_OK && data != null) {
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, data.data)
            val uri = Uri.parse(MediaStore.Images.Media.insertImage(requireContext().contentResolver, bitmap, null, null))
            viewModel.updateProfileAvatar(uri = uri)
        }
    }

    private val openCameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data = result.data
        if (result.resultCode == RESULT_OK && data != null) {
            val bitmap = data.extras?.get("data") as Bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ByteArrayOutputStream())
            val uri = Uri.parse(MediaStore.Images.Media.insertImage(requireContext().contentResolver, bitmap, null, null))
            viewModel.updateProfileAvatar(uri = uri)
        }
    }

    private fun signOutAfterConfirmation() = MaterialAlertDialogBuilder(requireContext())
        .setTitle(R.string.general_close_confirmation_title)
        .setMessage(R.string.general_sign_out_confirmation_message)
        .setPositiveButton(R.string.general_close_confirmation_positive) { _, _ -> viewModel.logOutUser() }
        .setNegativeButton(R.string.general_close_confirmation_negative) { _, _ -> viewModel.onDismissClicked() }
        .show()

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
