package com.matijasokol.notes.di

import com.matijasokol.notes.ui.components.Toast
import com.matijasokol.notes.ui.components.ToastAndroid
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val composeAppPlatformModule = module {
    factoryOf(::ToastAndroid) bind Toast::class
}
