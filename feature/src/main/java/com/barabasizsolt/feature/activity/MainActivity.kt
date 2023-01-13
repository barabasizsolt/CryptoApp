package com.barabasizsolt.feature.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.barabasizsolt.feature.R
import com.barabasizsolt.feature.databinding.ActivityMainBinding
import com.barabasizsolt.feature.screen.auth.AuthenticationFragment
import com.barabasizsolt.feature.screen.main.MainFragment
import com.barabasizsolt.feature.shared.navigation.BaseFragment
import com.barabasizsolt.feature.shared.navigation.Navigator
import com.barabasizsolt.feature.shared.utils.handleReplace
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), Navigator {

    private val viewModel by viewModel<MainActivityViewModel>()
    private val currentFragment get() = supportFragmentManager.findFragmentById(R.id.fragment_container) as? BaseFragment<*>?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        viewModel.event.onEach(::listenToEvent).launchIn(lifecycleScope)
        viewModel.getCurrentUser()
    }

    private fun listenToEvent(event: MainActivityViewModel.Event) = when (event) {
        is MainActivityViewModel.Event.NavigateToAuthentication -> navigateToAuthentication()
        is MainActivityViewModel.Event.NavigateToMain -> navigateToMain()
    }

    override fun navigateToMain() = supportFragmentManager.handleReplace(
        newInstance = MainFragment.Companion::newInstance
    )

    override fun navigateToAuthentication() = supportFragmentManager.handleReplace(
        newInstance = AuthenticationFragment.Companion::newInstance
    )

    override fun onBackPressed() {
        if (currentFragment?.onBackPressed() != true) {
            if (supportFragmentManager.backStackEntryCount >= 1) {
                super.onBackPressed()
            } else {
                closeAfterConfirmation()
            }
        }
    }

    private fun closeAfterConfirmation() = MaterialAlertDialogBuilder(this)
        .setTitle(R.string.general_close_confirmation_title)
        .setMessage(R.string.general_close_confirmation_message)
        .setPositiveButton(R.string.general_close_confirmation_positive) { _, _ -> supportFinishAfterTransition() }
        .setNegativeButton(R.string.general_close_confirmation_negative, null)
        .show()
}
