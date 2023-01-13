package com.barabasizsolt.feature.screen.auth

import android.os.Bundle
import android.view.View
import com.barabasizsolt.feature.R
import com.barabasizsolt.feature.databinding.FragmentAuthenticationBinding
import com.barabasizsolt.feature.screen.auth.login.LoginFragment
import com.barabasizsolt.feature.shared.navigation.BaseFragment
import com.barabasizsolt.feature.shared.utils.handleReplace

class AuthenticationFragment : BaseFragment<FragmentAuthenticationBinding>(R.layout.fragment_authentication) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null && currentFragment == null) {
            childFragmentManager.handleReplace(
                newInstance = LoginFragment.Companion::newInstance,
                tag = null
            )
        }
    }

    companion object {
        fun newInstance() = AuthenticationFragment()
    }
}
