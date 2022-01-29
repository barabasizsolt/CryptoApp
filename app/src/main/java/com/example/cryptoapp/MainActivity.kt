package com.example.cryptoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import com.example.cryptoapp.data.repository.Cache
import com.example.cryptoapp.databinding.ActivityMainBinding
import com.example.cryptoapp.feature.auth.AuthenticationFragment
import com.example.cryptoapp.feature.main.MainFragment
import com.example.cryptoapp.feature.main.cryptocurrency.Constant.CURRENCY_FIRE_STORE_PATH
import com.example.cryptoapp.feature.shared.navigation.BaseFragment
import com.example.cryptoapp.feature.shared.navigation.Navigator
import com.example.cryptoapp.feature.shared.utils.handleReplace
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), Navigator {

    private val currentFragment get() = supportFragmentManager.findFragmentById(R.id.fragment_container) as? BaseFragment<*>?
    lateinit var mAuth: FirebaseAuth
    lateinit var fireStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        fireStore = Firebase.firestore

        // supportActionBar?.hide()

        if (mAuth.currentUser == null) {
            navigateToAuthentication()
        } else {
            navigateToMain()
        }
    }

    fun getUserWatchLists() {
        fireStore.collection(CURRENCY_FIRE_STORE_PATH)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.data["userid"].toString() == mAuth.currentUser?.uid) {
                        Cache.addUserWatchList(document.data["uuid"].toString())
                    }
                }
            }
    }

    override fun navigateToMain() = supportFragmentManager.handleReplace(
        newInstance = MainFragment.Companion::newInstance
    ).also {
        getUserWatchLists()
    }

    override fun navigateToAuthentication() = supportFragmentManager.handleReplace(
        newInstance = AuthenticationFragment.Companion::newInstance
    )

    override fun onBackPressed() {
        if (currentFragment?.onBackPressed() != true) {
            if (supportFragmentManager.backStackEntryCount >= 1) {
                super.onBackPressed()
            } else {
                closeAfterConfirmation()
            }
        }
    }

    private fun closeAfterConfirmation() = MaterialAlertDialogBuilder(this)
        .setTitle(R.string.general_close_confirmation_title)
        .setMessage(R.string.general_close_confirmation_message)
        .setPositiveButton(R.string.general_close_confirmation_positive) { _, _ -> supportFinishAfterTransition() }
        .setNegativeButton(R.string.general_close_confirmation_negative, null)
        .show()
}
