package com.matijasokol.notes.di

import com.matijasokol.notes.core.presentation.Dictionary
import com.matijasokol.notes.core.presentation.DictionaryImpl
import com.matijasokol.notes.data.FakeNotesService
import com.matijasokol.notes.data.NotesRepositoryImpl
import com.matijasokol.notes.data.NotesService
import com.matijasokol.notes.domain.NotesRepository
import com.matijasokol.notes.navigation.Navigator
import com.matijasokol.notes.navigation.NavigatorImpl
import com.matijasokol.notes.presentation.list.NotesListUiMapper
import com.matijasokol.notes.presentation.list.NotesListViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val coreModule = module {
    factoryOf(::DictionaryImpl) bind Dictionary::class
    singleOf(::NavigatorImpl) bind Navigator::class
}

private val notesListModule = module {
    factoryOf(::NotesRepositoryImpl) bind NotesRepository::class
    factoryOf(::FakeNotesService) bind NotesService::class

    factoryOf(::NotesListUiMapper)
    viewModelOf(::NotesListViewModel)
}

val sharedModule = module {
    includes(
        coreModule,
        notesListModule,
    )
}
