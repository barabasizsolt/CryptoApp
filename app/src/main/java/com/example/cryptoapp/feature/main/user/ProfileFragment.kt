package com.example.cryptoapp.feature.main.user

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.View
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentProfileBinding
import com.example.cryptoapp.feature.shared.navigation.BaseFragment
import com.example.cryptoapp.feature.shared.utils.loadImage
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.updatePassword.hint = "New Password"
        val user = FirebaseAuth.getInstance().currentUser
        user?.photoUrl?.let { binding.userLogo.loadImage(it, R.drawable.ic_avatar) }
        binding.email.setText(user?.email)
        binding.registrationDate.setText(user?.metadata?.creationTimestamp?.formatUserRegistrationDate())
        binding.signOut.setOnClickListener { signOutAfterConfirmation() }
    }

    private fun signOutAfterConfirmation() = MaterialAlertDialogBuilder(requireContext())
        .setTitle(R.string.general_close_confirmation_title)
        .setMessage(R.string.general_sign_out_confirmation_message)
        .setPositiveButton(R.string.general_close_confirmation_positive) { _, _ ->
            FirebaseAuth.getInstance().signOut()
            navigator?.navigateToAuthentication()
        }
        .setNegativeButton(R.string.general_close_confirmation_negative, null)
        .show()

    private fun Long.formatUserRegistrationDate() = SimpleDateFormat("MMM dd, yyy", Locale.getDefault()).format(this)

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
