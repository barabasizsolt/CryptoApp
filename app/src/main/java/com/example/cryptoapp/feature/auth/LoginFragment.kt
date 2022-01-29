package com.example.cryptoapp.feature.auth

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentLoginBinding
import com.example.cryptoapp.feature.shared.navigation.BaseFragment
import com.example.cryptoapp.feature.shared.utils.handleReplace
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    private lateinit var customDialogView: View
    private lateinit var resetEmail: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // resetEmail = customDialogView.findViewById(R.id.reset_update_field)
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
        binding.forgotPassword.setOnClickListener {
            if (customDialogView.parent != null) {
                (customDialogView.parent as ViewGroup).removeView(customDialogView)
            }
            MaterialAlertDialogBuilder(requireContext())
                .setView(customDialogView)
                .setTitle(R.string.reset_password)
                .setNeutralButton(resources.getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(resources.getString(R.string.reset)) { _, _ ->
                    if (validateResetEmail()) {
                        binding.progressBar.visibility = View.VISIBLE

                        (activity as MainActivity).mAuth.sendPasswordResetEmail(resetEmail.text.toString())
                            .addOnCompleteListener(requireActivity()) { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(requireContext(), "Reset link sent to your email", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(requireContext(), "Unable to send reset mail", Toast.LENGTH_LONG).show()
                                }

                                binding.progressBar.visibility = View.INVISIBLE
                            }
                    }
                }
                .show()
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
