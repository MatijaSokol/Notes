package com.matijasokol.presentation.details

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.matijasokol.presentation.navigation.NavigationDestination

object DetailsDestination : NavigationDestination<Int> {

    const val ARGUMENT_NOTE_ID = "noteId"

    override fun route(): String = "details/{$ARGUMENT_NOTE_ID}"

    override fun buildRoute(data: Int): String = "details/$data"

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument(ARGUMENT_NOTE_ID) {
                type = NavType.IntType
            },
        )
}
