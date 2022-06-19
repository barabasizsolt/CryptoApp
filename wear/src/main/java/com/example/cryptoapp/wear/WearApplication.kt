package com.example.cryptoapp.wear

import android.app.Application
import com.example.cryptoapp.auth.di.createAuthenticationModules
import com.example.cryptoapp.auth.service.AuthenticationService
import com.example.cryptoapp.data.createDataModules
import com.example.cryptoapp.domain.domainModule
import com.example.cryptoapp.firestore.di.createFirestoreModule
import com.example.cryptoapp.firestore.service.FirestoreService
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WearApplication : Application() {

    private val authService: AuthenticationService by inject()
    private val fireStoreService: FirestoreService by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WearApplication)
            modules(
                modules = createDataModules(
                    coinRankingUrl = BuildConfig.COINRANKING_URL,
                    coinGekkoUrl = BuildConfig.COINGEKKO_URL
                ) + createAuthenticationModules() + createFirestoreModule().plus(listOf(domainModule))
            )
            authService.initialize(context = this@WearApplication)
            fireStoreService.initialize()
        }
    }
}