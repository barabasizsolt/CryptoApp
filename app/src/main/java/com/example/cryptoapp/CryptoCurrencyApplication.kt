package com.example.cryptoapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CryptoCurrencyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CryptoCurrencyApplication)
            modules(cryptoCurrencyModule)
        }
    }
}
