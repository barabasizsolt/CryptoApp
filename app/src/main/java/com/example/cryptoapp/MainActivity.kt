package com.example.cryptoapp

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.cryptoapp.data.constant.CryptoConstant
import com.example.cryptoapp.data.constant.CryptoConstant.loadImage
import com.example.cryptoapp.data.repository.Cache
import com.example.cryptoapp.databinding.ActivityMainBinding
import com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyList.CryptoCurrencyFragment
import com.example.cryptoapp.feature.event.EventFragment
import com.example.cryptoapp.feature.exchange.ExchangeFragment
import com.example.cryptoapp.feature.user.LoginFragment
import com.example.cryptoapp.feature.user.ProfileFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var topAppBar: MaterialToolbar
    lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navHeader: View
    private lateinit var userLogo: ImageView
    private lateinit var userEmail: TextView
    lateinit var mAuth: FirebaseAuth
    lateinit var fireStore: FirebaseFirestore
    lateinit var favoriteMenuItem: MenuItem
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        fireStore = Firebase.firestore

        supportActionBar?.hide()
        topAppBar = binding.topAppBar
        bottomNavigationView = binding.bottomNavigation

        topAppBar.visibility = View.GONE
        bottomNavigationView.visibility = View.GONE
        favoriteMenuItem = binding.topAppBar.menu[0]
        favoriteMenuItem.isVisible = false

        navHeader = binding.navigationView.getHeaderView(0)
        userLogo = navHeader.findViewById(R.id.user_logo)
        userLogo.loadImage(R.drawable.ic_avatar)
        userEmail = navHeader.findViewById(R.id.user_email)

        initBottomNavigation()
        if (mAuth.currentUser == null) {
            replaceFragment(LoginFragment(), R.id.activity_fragment_container)
        } else {
            initModalNavigationDrawer()
            getUserWatchLists()
            replaceFragment(CryptoCurrencyFragment(), R.id.activity_fragment_container)
        }
    }

    fun getUserWatchLists() {
        fireStore.collection(CryptoConstant.CURRENCY_FIRE_STORE_PATH)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.data["userid"].toString() == mAuth.currentUser?.uid) {
                        Cache.addUserWatchList(document.data["uuid"].toString())
                    }
                }
            }
    }

    private fun initBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.currencies -> {
                    replaceFragment(CryptoCurrencyFragment(), R.id.activity_fragment_container)
                    true
                }
                R.id.exchanges -> {
                    replaceFragment(ExchangeFragment(), R.id.activity_fragment_container)
                    true
                }
                R.id.favorites -> {
                    // TODO: implement it
                    true
                }
                R.id.events -> {
                    replaceFragment(EventFragment(), R.id.activity_fragment_container)
                    true
                }
                else -> false
            }
        }
    }

    fun initModalNavigationDrawer() {
        binding.topAppBar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            when (menuItem.itemId) {
                R.id.profile -> {
                    replaceFragment(ProfileFragment(), R.id.activity_fragment_container)
                }
                R.id.wallet -> {
                    // TODO: implement it
                }
                R.id.calculator -> {
                    // TODO: implement it
                }
                R.id.logout -> {
                    mAuth.signOut()
                    Cache.deleteUserWatchList()
                    binding.topAppBar.visibility = View.GONE
                    binding.bottomNavigation.visibility = View.GONE
                    replaceFragment(LoginFragment(), R.id.activity_fragment_container)
                }
            }
            binding.drawerLayout.close()
            true
        }
        mAuth.currentUser?.photoUrl?.let { userLogo.loadImage(it, R.drawable.ic_avatar) }
        userEmail.text = mAuth.currentUser?.email.toString()
    }

    fun replaceFragment(
        fragment: Fragment,
        containerId: Int,
        addToBackStack: Boolean = false,
        withAnimation: Boolean = false
    ) {
        val transaction = supportFragmentManager.beginTransaction()
        if (withAnimation) {
            transaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fade_out
            )
        }
        transaction.replace(containerId, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
}
