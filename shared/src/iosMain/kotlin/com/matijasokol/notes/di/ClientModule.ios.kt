package com.matijasokol.notes.di

import com.matijasokol.notes.data.database.DriverFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val clientModule = module {
    singleOf(::DriverFactory)
}
