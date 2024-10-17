package com.matijasokol.notes.di

import com.matijasokol.notes.navigation.Navigator
import com.matijasokol.notes.navigation.NavigatorImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sharedModule = module {
    singleOf(::NavigatorImpl) bind Navigator::class
}
