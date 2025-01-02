package com.matijasokol.notes.di

import com.matijasokol.notes.AppDispatchers
import com.matijasokol.notes.client.ClientDatabase
import com.matijasokol.notes.data.auth.FirebaseAuthProvider
import com.matijasokol.notes.data.client.buildHttpClient
import com.matijasokol.notes.data.client.httpClientEngineFactory
import com.matijasokol.notes.data.client.json
import com.matijasokol.notes.data.client.withTokenInterceptor
import com.matijasokol.notes.data.database.DriverFactory
import com.matijasokol.notes.data.database.NoteDao
import com.matijasokol.notes.data.database.NoteDaoImpl
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

private val databaseModule = module {
    single { ClientDatabase(driver = get<DriverFactory>().createDriver()) }
    factory { get<ClientDatabase>().noteQueries }
    factoryOf(::NoteDaoImpl) bind NoteDao::class
}

private val coreModule = module {
    factory { AppDispatchers() }
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
        databaseModule,
        coreModule,
        notesModule,
        userModule,
        clientModule,
    )
}
