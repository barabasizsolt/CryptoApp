package com.example.cryptoapp

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import com.example.cryptoapp.data.dataModule
import com.example.cryptoapp.domain.domainModule
import com.example.cryptoapp.feature.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CryptoCurrencyApplication : Application(){

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CryptoCurrencyApplication)
            modules(mainActivityModule, dataModule, domainModule, presentationModule)
        }
    }
}
