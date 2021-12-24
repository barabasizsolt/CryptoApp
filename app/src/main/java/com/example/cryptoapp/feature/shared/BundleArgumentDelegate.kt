package com.example.cryptoapp.feature.shared

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

sealed class BundleArgumentDelegate<T>(protected val key: kotlin.String, protected val defaultValue: T) : ReadWriteProperty<Bundle?, T> {

    class String(key: kotlin.String, defaultValue: kotlin.String = "") : BundleArgumentDelegate<kotlin.String>(key, defaultValue) {

        override fun getValue(thisRef: Bundle?, property: KProperty<*>) = thisRef?.getString(key, defaultValue) ?: defaultValue

        override fun setValue(thisRef: Bundle?, property: KProperty<*>, value: kotlin.String) = thisRef?.putString(key, value) ?: Unit
    }
}
