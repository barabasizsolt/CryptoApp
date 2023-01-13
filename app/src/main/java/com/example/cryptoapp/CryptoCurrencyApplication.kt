package com.example.cryptoapp

import android.app.Application
import com.example.cryptoapp.data.createDataModules
import com.example.cryptoapp.domain.domainModule
import com.example.cryptoapp.feature.presentationModule
import com.example.cryptoapp.auth.di.createAuthenticationModules
import com.example.cryptoapp.auth.service.AuthenticationService
import com.example.cryptoapp.storage.di.createFirestoreModule
import com.example.cryptoapp.storage.service.FirestoreService
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CryptoCurrencyApplication : Application() {

    private val authService: AuthenticationService by inject()
    private val fireStoreService: FirestoreService by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CryptoCurrencyApplication)
            modules(
                modules = createDataModules(
                    coinRankingUrl = BuildConfig.COINRANKING_URL,
                    coinGekkoUrl = BuildConfig.COINGEKKO_URL
                ) + createAuthenticationModules() + createFirestoreModule().plus(listOf(domainModule, presentationModule))
            )
            authService.initialize(context = this@CryptoCurrencyApplication)
            fireStoreService.initialize()
        }
    }
}
