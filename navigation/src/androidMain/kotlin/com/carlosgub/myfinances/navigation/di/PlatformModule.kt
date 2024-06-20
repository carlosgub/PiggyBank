package com.carlosgub.myfinances.navigation.di

import com.carlosgub.myfinances.data.sqldelight.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module =
    module {
        single { DatabaseDriverFactory(get()) }
    }
