package com.matijasokol.notes.di

import com.matijasokol.notes.auth.AuthViewModel
import com.matijasokol.notes.list.NoteListUiMapper
import com.matijasokol.notes.list.NoteListViewModel
import com.matijasokol.notes.navigation.Navigator
import com.matijasokol.notes.navigation.NavigatorImpl
import com.matijasokol.notes.splash.SplashViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val splashModule = module {
    viewModelOf(::SplashViewModel)
}

private val authModule = module {
    viewModelOf(::AuthViewModel)
}

private val noteListModule = module {
    viewModelOf(::NoteListViewModel)
    factoryOf(::NoteListUiMapper)
}

val composeAppSharedModule = module {
    singleOf(::NavigatorImpl) bind Navigator::class

    includes(
        composeAppPlatformModule,
        splashModule,
        authModule,
        noteListModule,
    )
}
