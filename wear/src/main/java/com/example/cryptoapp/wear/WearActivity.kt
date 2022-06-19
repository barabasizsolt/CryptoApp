package com.example.cryptoapp.wear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cryptoapp.wear.screen.auth.AuthScreen
import com.example.cryptoapp.wear.screen.auth.rememberAuthScreenState

class WearActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthScreen(authScreenState = rememberAuthScreenState())
        }
    }
}