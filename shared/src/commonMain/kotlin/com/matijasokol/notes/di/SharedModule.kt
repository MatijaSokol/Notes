package com.matijasokol.notes.di

import com.matijasokol.notes.domain.UUIDProvider
import com.matijasokol.notes.domain.UUIDProviderImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sharedModule = module {
    factoryOf(::UUIDProviderImpl) bind UUIDProvider::class
}
