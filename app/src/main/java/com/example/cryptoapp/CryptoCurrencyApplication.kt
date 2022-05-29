package com.example.cryptoapp

import android.app.Application
import com.example.cryptoapp.data.createDataModules
import com.example.cryptoapp.domain.domainModule
import com.example.cryptoapp.feature.presentationModule
import com.example.cryptoapp.firebase.di.createAuthenticationModules
import com.example.cryptoapp.firebase.di.createFireStoreModules
import com.example.cryptoapp.firestore.di.fireStoreModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CryptoCurrencyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CryptoCurrencyApplication)
            modules(
                modules = createDataModules(
                    coinRankingUrl = BuildConfig.COINRANKING_URL,
                    coinGekkoUrl = BuildConfig.COINGEKKO_URL
                ) +
                        createAuthenticationModules() +
                        createFireStoreModules() +
                        fireStoreModule().plus(listOf(domainModule, presentationModule))
            )
        }
    }
}
