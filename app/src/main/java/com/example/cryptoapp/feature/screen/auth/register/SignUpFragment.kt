package com.example.cryptoapp.feature.screen.auth.register

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.cryptoapp.BR
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentSignUpBinding
import com.example.cryptoapp.feature.shared.navigation.BaseFragment
import com.example.cryptoapp.feature.shared.utils.createSnackBar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

// TODO[mid] refactor the xml
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    private val viewModel by viewModel<SignUpViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.setVariable(BR.viewModel, viewModel)
        viewModel.event.onEach(::listenToEvent).launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun listenToEvent(event: SignUpViewModel.Event) = when (event) {
        is SignUpViewModel.Event.ShowErrorMessage -> binding.root.createSnackBar(message = event.message)
        is SignUpViewModel.Event.RegisterUser -> navigator?.navigateToMain()
    }

    companion object {
        fun newInstance() = SignUpFragment()
    }
}
