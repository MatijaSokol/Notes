package com.matijasokol.presentation.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import java.net.URLEncoder

fun interface NavigationDestination<T> {

    fun route(): String
    fun buildRoute(data: T): String = route()
    fun retrieveRouteData(): T? = null
    val arguments: List<NamedNavArgument>
        get() = emptyList()

    val deepLinks: List<NavDeepLink>
        get() = emptyList()

    @Suppress("UnnecessaryAbstractClass")
    abstract class ParamType<T> : NavType<T>(isNullableAllowed = false)

    fun String.encodeUrl(): String = URLEncoder.encode(this, UTF_8_ENC)
}

private const val UTF_8_ENC = "UTF-8"
