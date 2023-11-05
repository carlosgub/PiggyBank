package com.carlosgub.myfinance.app

import MainView
import android.os.Bundle
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import di.initKoin
import moe.tlaster.precompose.lifecycle.PreComposeActivity
import moe.tlaster.precompose.lifecycle.setContent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.stopKoin

class MainActivity : PreComposeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initKoin {
            androidContext(applicationContext)
        }
        Firebase.initialize(this)
        setContent {
            MainView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }
}
