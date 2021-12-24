package com.example.cryptoapp.feature.user

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentProfileBinding
import com.example.cryptoapp.feature.shared.loadImage
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import java.util.Locale

class ProfileFragment : Fragment() {
    private lateinit var customDialogView: View
    private lateinit var binding: FragmentProfileBinding
    private lateinit var newPassword: TextView
    private lateinit var newPasswordLayout: TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        customDialogView = inflater.inflate(R.layout.reset_update_dialog_layout, null, false)
        initUI()
        return binding.root
    }

    private fun initUI() {
        newPassword = customDialogView.findViewById(R.id.reset_update_field)
        newPasswordLayout = customDialogView.findViewById(R.id.reset_update_layout)
        binding.updatePassword.hint = "New Password"
        val user = (activity as MainActivity).mAuth.currentUser
        user?.photoUrl?.let { binding.userLogo.loadImage(it, R.drawable.ic_avatar) }
        binding.email.setText(user?.email)
        binding.registrationDate.setText(user?.metadata?.creationTimestamp?.formatUserRegistrationDate())
        binding.updatePassword.setOnClickListener {
            if (customDialogView.parent != null) {
                (customDialogView.parent as ViewGroup).removeView(customDialogView)
            }
            MaterialAlertDialogBuilder(requireContext())
                .setView(customDialogView)
                .setTitle(R.string.update_password)
                .setNeutralButton(resources.getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(resources.getString(R.string.update)) { _, _ ->
                    if (validateNewPassword()) {
                        binding.progressBar.visibility = View.VISIBLE

                        user?.updatePassword(newPassword.text.toString())
                            ?.addOnCompleteListener(requireActivity()) { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(requireContext(), "Password changes successfully", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(requireContext(), "Short password\n(Minimum 6 characters)", Toast.LENGTH_LONG).show()
                                    newPasswordLayout.error = null
                                }

                                binding.progressBar.visibility = View.INVISIBLE
                            }
                    }
                }
                .show()
        }
    }

    private fun validateNewPassword(): Boolean {
        when {
            newPassword.text.toString().isEmpty() -> {
                newPasswordLayout.error = getString(R.string.error)
                return false
            }
        }
        return true
    }

    private fun Long.formatUserRegistrationDate() = SimpleDateFormat("MMM dd, yyy", Locale.getDefault()).format(this)

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
