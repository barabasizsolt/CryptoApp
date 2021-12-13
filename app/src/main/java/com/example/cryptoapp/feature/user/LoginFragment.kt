package com.example.cryptoapp.feature.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentLoginBinding
import com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyList.CryptoCurrencyFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {
    private lateinit var progressBar: ProgressBar
    private lateinit var loginButton: Button
    private lateinit var signUp: TextView
    private lateinit var forgotPassword: TextView
    private lateinit var email: TextView
    private lateinit var password: TextView
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var customDialogView: View
    private lateinit var resetEmail: TextView
    private lateinit var resetEmailLayout: TextInputLayout

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        customDialogView = inflater.inflate(R.layout.reset_update_dialog_layout, null, false)

        bindUI(view)
        initUI()

        return view
    }

    private fun bindUI(view: View) {
        progressBar = view.findViewById(R.id.progress_bar)
        loginButton = view.findViewById(R.id.login_button)
        signUp = view.findViewById(R.id.sign_up)
        forgotPassword = view.findViewById(R.id.forgot_password)
        email = view.findViewById(R.id.email)
        password = view.findViewById(R.id.password)
        emailLayout = view.findViewById(R.id.email_layout)
        passwordLayout = view.findViewById(R.id.password_layout)
        resetEmail = customDialogView.findViewById(R.id.reset_update_field)
        resetEmailLayout = customDialogView.findViewById(R.id.reset_update_layout)
    }

    private fun initUI() {
        resetEmailLayout.hint = "Email"
        loginButton.setOnClickListener {
            if (validateInput()) {
                progressBar.visibility = View.VISIBLE
                (activity as MainActivity).mAuth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
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
                        progressBar.visibility = View.INVISIBLE
                    }
            }
        }
        forgotPassword.setOnClickListener {
            if (customDialogView.parent != null) {
                (customDialogView.parent as ViewGroup).removeView(customDialogView)
            }
            MaterialAlertDialogBuilder(requireContext())
                .setView(customDialogView)
                .setTitle(R.string.reset_password)
                .setNeutralButton(resources.getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(resources.getString(R.string.reset)) { _, _ ->
                    if (validateResetEmail()) {
                        progressBar.visibility = View.VISIBLE

                        (activity as MainActivity).mAuth.sendPasswordResetEmail(resetEmail.text.toString())
                            .addOnCompleteListener(requireActivity()) { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(requireContext(), "Reset link sent to your email", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(requireContext(), "Unable to send reset mail", Toast.LENGTH_LONG).show()
                                }

                                progressBar.visibility = View.INVISIBLE
                            }
                    }
                }
                .show()
        }
        signUp.setOnClickListener {
            (activity as MainActivity).replaceFragment(SignUpFragment(), R.id.activity_fragment_container, addToBackStack = true)
        }
    }

    private fun validateInput(): Boolean {
        emailLayout.error = null
        passwordLayout.error = null

        when {
            email.text.toString().isEmpty() -> {
                emailLayout.error = getString(R.string.error)
                return false
            }
            password.text.toString().isEmpty() -> {
                passwordLayout.error = getString(R.string.error)
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
