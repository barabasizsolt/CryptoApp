package com.example.cryptoapp.feature.user

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.data.constant.CryptoConstant.loadImage
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import java.util.Locale

class ProfileFragment : Fragment() {
    private lateinit var progressBar: ProgressBar
    private lateinit var userLogo: ImageView
    private lateinit var userEmail: TextView
    private lateinit var registrationDate: TextView
    private lateinit var updatePassword: TextView
    private lateinit var customDialogView: View
    private lateinit var newPassword: TextView
    private lateinit var newPasswordLayout: TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        customDialogView = inflater.inflate(R.layout.reset_update_dialog_layout, null, false)

        progressBar = view.findViewById(R.id.progress_bar)
        userLogo = view.findViewById(R.id.user_logo)
        userEmail = view.findViewById(R.id.email)
        registrationDate = view.findViewById(R.id.registration_date)
        updatePassword = view.findViewById(R.id.update_password)
        newPassword = customDialogView.findViewById(R.id.reset_update_field)
        newPasswordLayout = customDialogView.findViewById(R.id.reset_update_layout)
        initUI()

        return view
    }

    private fun initUI() {
        newPasswordLayout.hint = "New Password"
        val user = (activity as MainActivity).mAuth.currentUser
        user?.photoUrl?.let { userLogo.loadImage(it, R.drawable.ic_avatar) }
        userEmail.text = user?.email
        registrationDate.text = user?.metadata?.creationTimestamp?.let { getDate(it) }
        updatePassword.setOnClickListener {
            if (customDialogView.parent != null) {
                (customDialogView.parent as ViewGroup).removeView(customDialogView)
            }
            MaterialAlertDialogBuilder(requireContext())
                .setView(customDialogView)
                .setTitle(R.string.update_password)
                .setNeutralButton(resources.getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(resources.getString(R.string.update)) { _, _ ->
                    if (validateNewPassword()) {
                        progressBar.visibility = View.VISIBLE

                        user?.updatePassword(newPassword.text.toString())
                            ?.addOnCompleteListener(requireActivity()) { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(requireContext(), "Password changes successfully", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(requireContext(), "Short password\n(Minimum 6 characters)", Toast.LENGTH_LONG).show()
                                }

                                progressBar.visibility = View.INVISIBLE
                            }
                    }
                }
                .show()
        }
    }

    private fun validateNewPassword(): Boolean {
        newPasswordLayout.error = null

        when {
            newPassword.text.toString().isEmpty() -> {
                newPasswordLayout.error = getString(R.string.error)
                return false
            }
        }
        return true
    }

    private fun getDate(timeStamp: Long): String? {
        val formatter = SimpleDateFormat("MMM dd, yyy", Locale.getDefault())
        return formatter.format(timeStamp)
    }
}
