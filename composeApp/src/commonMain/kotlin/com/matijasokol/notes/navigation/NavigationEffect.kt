package com.matijasokol.notes.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import org.koin.compose.koinInject

@Composable
fun NavigationEffect(
    navController: NavHostController,
    navigator: Navigator = koinInject(),
) {
    LaunchedEffect(navController) {
        navigator.navigationEvent
            .collect { executeNavigationRequests(navController, it) }
    }
}

private fun executeNavigationRequests(
    navController: NavHostController,
    navigationEvent: NavigationEvent,
) {
    when (navigationEvent) {
        is NavigationEvent.Destination<*> -> navController.navigate(
            route = navigationEvent.route as Any,
            builder = navigationEvent.builder,
        )
        NavigationEvent.NavigateUp -> navController.navigateUp()
    }
}
