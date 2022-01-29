package com.example.cryptoapp.feature.auth

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentLoginBinding
import com.example.cryptoapp.feature.shared.navigation.BaseFragment
import com.example.cryptoapp.feature.shared.utils.handleReplace

class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private lateinit var resetEmail: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.emailLayout.hint = "Email"
        binding.loginButton.setOnClickListener {
            if (validateInput()) {
                binding.progressBar.visibility = View.VISIBLE
                (activity as MainActivity).mAuth.signInWithEmailAndPassword(binding.email.text.toString(), binding.password.text.toString())
                    .addOnCompleteListener(
                        requireActivity()
                    ) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                requireActivity(),
                                "Successfully logged in",
                                Toast.LENGTH_LONG
                            ).show()
                            navigator?.navigateToMain()
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "Incorrect email or password",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        binding.progressBar.visibility = View.INVISIBLE
                    }
            }
        }
        binding.signUp.setOnClickListener {
            parentFragment?.childFragmentManager?.handleReplace(
                addToBackStack = true,
                newInstance = SignUpFragment.Companion::newInstance
            )
        }
    }

    private fun validateInput(): Boolean {
        binding.emailLayout.error = null
        binding.passwordLayout.error = null

        when {
            binding.email.text.toString().isEmpty() -> {
                binding.emailLayout.error = getString(R.string.error)
                return false
            }
            binding.password.text.toString().isEmpty() -> {
                binding.passwordLayout.error = getString(R.string.error)
                return false
            }
        }
        return true
    }

    private fun validateResetEmail(): Boolean {
        when {
            resetEmail.text.toString().isEmpty() -> {
                return false
            }
        }
        return true
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}
