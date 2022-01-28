package com.example.cryptoapp.feature.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentSignUpBinding
import com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyList.CryptoCurrencyFragment
import com.example.cryptoapp.feature.shared.utils.handleReplace

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        initUI()
        return binding.root
    }

    private fun initUI() {
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
                            val fm = (activity as MainActivity).supportFragmentManager
                            for (i in 0 until fm.backStackEntryCount) {
                                fm.popBackStack()
                            }
                            (activity as MainActivity).initModalNavigationDrawer()
                            activity?.supportFragmentManager?.handleReplace { CryptoCurrencyFragment.newInstance() }
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
