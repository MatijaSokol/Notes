package com.matijasokol.notes.di

import com.matijasokol.notes.data.auth.FirebaseAuthProvider
import com.matijasokol.notes.data.client.buildHttpClient
import com.matijasokol.notes.data.client.httpClientEngineFactory
import com.matijasokol.notes.data.client.json
import com.matijasokol.notes.data.notes.NotesRepositoryImpl
import com.matijasokol.notes.domain.auth.AuthProvider
import com.matijasokol.notes.domain.notes.NotesRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val networkModule = module {
    factory { json }
    factoryOf(::httpClientEngineFactory)
    factoryOf(::buildHttpClient)
}

private val notesModule = module {
    factoryOf(::NotesRepositoryImpl) bind NotesRepository::class
}

actual val platformModule = module {
    factoryOf(::FirebaseAuthProvider) bind AuthProvider::class

    includes(
        networkModule,
        notesModule,
    )
}
