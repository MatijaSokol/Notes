package com.matijasokol.notes.server.plugins

import com.matijasokol.notes.di.sharedModule
import com.matijasokol.notes.server.ServerDatabase
import com.matijasokol.notes.server.database.provideDatabase
import com.matijasokol.notes.server.note.service.NoteService
import com.matijasokol.notes.server.note.service.NoteServiceImpl
import com.matijasokol.notes.server.note.usecase.CreateNote
import com.matijasokol.notes.server.note.usecase.GetNoteById
import com.matijasokol.notes.server.note.usecase.GetUserNotes
import com.matijasokol.notes.server.user.service.UserService
import com.matijasokol.notes.server.user.service.UserServiceImpl
import com.matijasokol.notes.server.user.usecase.CreateUser
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(
            module { single { this@configureKoin } },
            sharedModule,
            serverModule,
        )
    }
}

private val databaseModule = module {
    singleOf(::provideDatabase)
    factory { get<ServerDatabase>().userQueries }
    factory { get<ServerDatabase>().noteQueries }
}

private val userModule = module {
    factoryOf(::CreateUser)
    factoryOf(::UserServiceImpl) bind UserService::class
}

private val noteModule = module {
    factoryOf(::CreateNote)
    factoryOf(::GetUserNotes)
    factoryOf(::GetNoteById)
    factoryOf(::NoteServiceImpl) bind NoteService::class
}

private val serverModule = module {
    includes(
        databaseModule,
        userModule,
        noteModule,
    )
}
