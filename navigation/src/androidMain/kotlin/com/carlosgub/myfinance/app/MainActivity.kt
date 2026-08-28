package com.carlosgub.myfinance.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.carlosgub.myfinances.navigation.MainView
import com.carlosgub.myfinances.navigation.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.stopKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initKoin {
            androidContext(applicationContext)
        }
        setContent {
            MainView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }
}
