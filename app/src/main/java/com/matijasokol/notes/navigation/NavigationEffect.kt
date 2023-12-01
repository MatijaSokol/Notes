package com.matijasokol.notes.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.compose.koinInject

@Composable
fun NavigationEffect(
    navController: NavHostController,
    navigator: Navigator = koinInject(),
) {
    LaunchedEffect(navController) {
        navigator.navigationEvent
            .onEach { executeNavigationRequests(navController, it) }
            .launchIn(this)
    }
}

private fun executeNavigationRequests(
    navController: NavHostController,
    navigationEvent: NavigationEvent,
) {
    when (navigationEvent) {
        is NavigationEvent.Destination -> navController.navigate(
            route = navigationEvent.destination,
            builder = navigationEvent.builder,
        )
        NavigationEvent.NavigateUp -> navController.popBackStack()
    }
}
