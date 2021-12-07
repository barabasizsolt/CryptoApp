package com.example.cryptoapp

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.cryptoapp.data.repository.CryptoApiRepository
import com.example.cryptoapp.feature.viewModel.CryptoApiViewModel
import com.example.cryptoapp.data.repository.Cache
import com.example.cryptoapp.data.constant.CryptoConstant
import com.example.cryptoapp.feature.cryptocurrency.CryptoCurrencyFragment
import com.example.cryptoapp.feature.event.EventFragment
import com.example.cryptoapp.feature.exchange.ExchangeFragment
import com.example.cryptoapp.feature.user.LoginFragment
import com.example.cryptoapp.feature.user.ProfileFragment
import com.example.cryptoapp.data.model.cryptoCurrency.AllCryptoCurrencies
import com.example.cryptoapp.data.model.cryptoCurrency.CryptoCurrency
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: CryptoApiViewModel
    private lateinit var navHeader: View
    private lateinit var userLogo: ImageView
    private lateinit var userEmail: TextView
    lateinit var mAuth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    lateinit var favoriteMenuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        firestore = Firebase.firestore

        supportActionBar?.hide()
        topAppBar.visibility = View.GONE
        bottomNavigation.visibility = View.GONE
        favoriteMenuItem = topAppBar.menu[0]
        favoriteMenuItem.isVisible = false

        navHeader = navigationView.getHeaderView(0)
        userLogo = navHeader.findViewById(R.id.user_logo)
        userEmail = navHeader.findViewById(R.id.user_email)

        viewModel = CryptoApiViewModel(CryptoApiRepository())
        viewModel.getAllCryptoCurrencies()
        viewModel.allCryptoCurrenciesResponse.observe(this, mainObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.allCryptoCurrenciesResponse.removeObserver(mainObserver)
    }

    private val mainObserver =
        androidx.lifecycle.Observer<Response<AllCryptoCurrencies>> { response ->
            if (response.isSuccessful) {
                Log.d("Observed", response.body()?.data.toString())
                Cache.setCryptoCurrencies(response.body()?.data?.coins as MutableList<CryptoCurrency>)
                initBottomNavigation()

                if (mAuth.currentUser == null) {
                    replaceFragment(LoginFragment(), R.id.activity_fragment_container)
                } else {
                    initModalNavigationDrawer()
                    getUserWatchLists()
                    replaceFragment(CryptoCurrencyFragment(), R.id.activity_fragment_container)
                }
            }
        }

    fun getUserWatchLists() {
        firestore.collection(CryptoConstant.CURRENCY_FIRE_STORE_PATH)
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
        bottomNavigation.setOnItemSelectedListener { item ->
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
                    //TODO: implement it
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
        topAppBar.setNavigationOnClickListener {
            drawerLayout.open()
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            when (menuItem.itemId) {
                R.id.profile -> {
                    replaceFragment(ProfileFragment(), R.id.activity_fragment_container)
                }
                R.id.wallet -> {
                    //TODO: implement it
                }
                R.id.calculator -> {
                    //TODO: implement it
                }
                R.id.logout -> {
                    mAuth.signOut()
                    Cache.deleteUserWatchList()
                    topAppBar.visibility = View.GONE
                    bottomNavigation.visibility = View.GONE
                    replaceFragment(LoginFragment(), R.id.activity_fragment_container)
                }
            }
            drawerLayout.close()
            true
        }

        Glide.with(this).load(mAuth.currentUser?.photoUrl).placeholder(R.drawable.ic_avataaars)
            .circleCrop().into(userLogo)
        userEmail.text = mAuth.currentUser?.email.toString()
    }

    fun replaceFragment(
        fragment: Fragment,
        containerId: Int,
        addToBackStack: Boolean = false,
        withAnimation: Boolean = false
    ) {
        val transaction = supportFragmentManager.beginTransaction()
        when (withAnimation) {
            true -> {
                transaction.setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.fade_out
                )
            }
        }
        transaction.replace(containerId, fragment)
        when (addToBackStack) {
            true -> {
                transaction.addToBackStack(null)
            }
        }
        transaction.commit()
    }

    fun getViewModel() = viewModel
}