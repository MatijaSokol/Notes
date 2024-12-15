package com.matijasokol.notes.di

import com.matijasokol.notes.data.auth.FirebaseAuthProvider
import com.matijasokol.notes.data.client.buildHttpClient
import com.matijasokol.notes.data.client.httpClientEngineFactory
import com.matijasokol.notes.data.client.json
import com.matijasokol.notes.data.client.withTokenInterceptor
import com.matijasokol.notes.data.notes.NotesRepositoryImpl
import com.matijasokol.notes.data.user.UserRepositoryImpl
import com.matijasokol.notes.domain.auth.AuthProvider
import com.matijasokol.notes.domain.notes.NotesRepository
import com.matijasokol.notes.domain.user.RegisterUser
import com.matijasokol.notes.domain.user.UserRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val networkModule = module {
    factory { json }
    factoryOf(::httpClientEngineFactory)
    factory { buildHttpClient(get(), get()).withTokenInterceptor(get()) }
}

private val notesModule = module {
    factoryOf(::NotesRepositoryImpl) bind NotesRepository::class
}

private val userModule = module {
    factoryOf(::UserRepositoryImpl) bind UserRepository::class

    factoryOf(::RegisterUser)
}

actual val platformModule = module {
    factoryOf(::FirebaseAuthProvider) bind AuthProvider::class

    includes(
        networkModule,
        notesModule,
        userModule,
    )
}
