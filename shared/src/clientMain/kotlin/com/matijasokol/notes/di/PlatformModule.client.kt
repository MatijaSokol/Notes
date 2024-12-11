package com.matijasokol.notes.di

import com.matijasokol.notes.data.auth.FirebaseAuthProvider
import com.matijasokol.notes.domain.auth.AuthProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {
    factoryOf(::FirebaseAuthProvider) bind AuthProvider::class
}
