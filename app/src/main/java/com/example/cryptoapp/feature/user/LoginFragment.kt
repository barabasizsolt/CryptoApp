package com.example.cryptoapp.feature.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentLoginBinding
import com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyList.CryptoCurrencyFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoginFragment : Fragment() {
    private lateinit var customDialogView: View
    private lateinit var binding: FragmentLoginBinding
    private lateinit var resetEmail: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        customDialogView = inflater.inflate(R.layout.reset_update_dialog_layout, null, false)
        initUI()
        return binding.root
    }

    private fun initUI() {
        resetEmail = customDialogView.findViewById(R.id.reset_update_field)
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
                            (activity as MainActivity).bottomNavigationView.selectedItemId = R.id.currencies
                            (activity as MainActivity).initModalNavigationDrawer()
                            (activity as MainActivity).getUserWatchLists()
                            (activity as MainActivity).replaceFragment(
                                CryptoCurrencyFragment(),
                                R.id.activity_fragment_container
                            )
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
            (activity as MainActivity).replaceFragment(SignUpFragment(), R.id.activity_fragment_container, addToBackStack = true)
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
}
