package com.example.cryptoapp.feature.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentSignUpBinding
import com.example.cryptoapp.feature.shared.navigation.BaseFragment

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.signUpButton.setOnClickListener {
            if (validateInput()) {
                binding.progressBar.visibility = View.VISIBLE
                (activity as MainActivity).mAuth.createUserWithEmailAndPassword(binding.email.text.toString(), binding.password.text.toString())
                    .addOnCompleteListener(
                        requireActivity()
                    ) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Successfully Registered",
                                Toast.LENGTH_LONG
                            ).show()
                            navigator?.navigateToMain()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Incorrect email or short password\n(Minimum 6 characters)",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        binding.progressBar.visibility = View.INVISIBLE
                    }
            }
        }
    }

    private fun validateInput(): Boolean {
        binding.emailLayout.error = null
        binding.passwordLayout.error = null
        binding.confirmPasswordLayout.error = null

        when {
            binding.email.text.toString().isEmpty() -> {
                binding.emailLayout.error = getString(R.string.error)
                return false
            }
            binding.password.text.toString().isEmpty() -> {
                binding.passwordLayout.error = getString(R.string.error)
                return false
            }
            binding.password.text.toString() != binding.confirmPassword.text.toString() -> {
                binding.confirmPasswordLayout.error = getString(R.string.password_error)
                return false
            }
        }
        return true
    }

    companion object {
        fun newInstance() = SignUpFragment()
    }
}
