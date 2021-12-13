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
import com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyList.CryptoCurrencyFragment
import com.google.android.material.textfield.TextInputLayout

class SignUpFragment : Fragment() {
    private lateinit var progressBar: ProgressBar
    private lateinit var signUpButton: Button
    private lateinit var email: TextView
    private lateinit var password: TextView
    private lateinit var confirmPassword: TextView
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var confirmPasswordLayout: TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        bindUI(view)
        initUI()
        return view
    }

    private fun bindUI(view: View) {
        progressBar = view.findViewById(R.id.progress_bar)
        signUpButton = view.findViewById(R.id.sign_up_button)
        email = view.findViewById(R.id.email)
        password = view.findViewById(R.id.password)
        confirmPassword = view.findViewById(R.id.confirm_password)
        emailLayout = view.findViewById(R.id.email_layout)
        passwordLayout = view.findViewById(R.id.password_layout)
        confirmPasswordLayout = view.findViewById(R.id.confirm_password_layout)
    }

    private fun initUI() {
        signUpButton.setOnClickListener {
            if (validateInput()) {
                progressBar.visibility = View.VISIBLE
                (activity as MainActivity).mAuth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(
                        requireActivity()
                    ) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Successfully Registered",
                                Toast.LENGTH_LONG
                            ).show()
                            val fm = (activity as MainActivity).supportFragmentManager
                            for (i in 0 until fm.backStackEntryCount) {
                                fm.popBackStack()
                            }
                            (activity as MainActivity).initModalNavigationDrawer()
                            (activity as MainActivity).replaceFragment(
                                CryptoCurrencyFragment(),
                                R.id.activity_fragment_container
                            )
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Incorrect email or short password\n(Minimum 6 characters)",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        progressBar.visibility = View.INVISIBLE
                    }
            }
        }
    }

    private fun validateInput(): Boolean {
        emailLayout.error = null
        passwordLayout.error = null
        confirmPasswordLayout.error = null

        when {
            email.text.toString().isEmpty() -> {
                emailLayout.error = getString(R.string.error)
                return false
            }
            password.text.toString().isEmpty() -> {
                passwordLayout.error = getString(R.string.error)
                return false
            }
            password.text.toString() != confirmPassword.text.toString() -> {
                confirmPasswordLayout.error = getString(R.string.password_error)
                return false
            }
        }
        return true
    }
}
