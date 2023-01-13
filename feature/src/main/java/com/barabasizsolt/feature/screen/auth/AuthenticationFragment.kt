package com.barabasizsolt.feature.screen.auth

import android.os.Bundle
import android.view.View
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentAuthenticationBinding
import com.example.cryptoapp.feature.screen.auth.login.LoginFragment
import com.example.cryptoapp.feature.shared.navigation.BaseFragment
import com.example.cryptoapp.feature.shared.utils.handleReplace

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
