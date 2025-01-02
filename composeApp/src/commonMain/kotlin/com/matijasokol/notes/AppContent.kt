package com.matijasokol.notes

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.matijasokol.notes.auth.AuthAction.LoginError
import com.matijasokol.notes.auth.AuthAction.LoginSuccess
import com.matijasokol.notes.auth.AuthAction.RegistrationError
import com.matijasokol.notes.auth.AuthAction.RegistrationSuccess
import com.matijasokol.notes.auth.AuthScreen
import com.matijasokol.notes.auth.AuthViewModel
import com.matijasokol.notes.details.DetailsScreen
import com.matijasokol.notes.details.NoteDetailsAction
import com.matijasokol.notes.details.NoteDetailsViewModel
import com.matijasokol.notes.list.ListScreen
import com.matijasokol.notes.list.NoteListAction
import com.matijasokol.notes.list.NoteListViewModel
import com.matijasokol.notes.navigation.Destination
import com.matijasokol.notes.navigation.NavigationEffect
import com.matijasokol.notes.navigation.NavigationEvent
import com.matijasokol.notes.navigation.Navigator
import com.matijasokol.notes.ui.components.LocalAnimatedContentScope
import com.matijasokol.notes.ui.components.LocalSharedTransitionScope
import com.matijasokol.notes.ui.theme.NotesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppContent(
    loggedIn: Boolean,
    navController: NavHostController = rememberNavController(),
    navigator: Navigator = koinInject(),
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    KoinContext {
        NotesTheme {
            NavigationEffect(navController)

            SharedTransitionLayout {
                CompositionLocalProvider(
                    LocalSharedTransitionScope provides this,
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = when (loggedIn) {
                            true -> Destination.List
                            false -> Destination.Auth
                        },
                    ) {
                        Auth(navigator)
                        List(navigator)
                        Details(scope, navigator)
                    }
                }
            }
        }
    }
}

private fun NavGraphBuilder.Auth(
    navigator: Navigator,
) {
    composable<Destination.Auth> {
        val viewmodel: AuthViewModel = koinViewModel()
        val state by viewmodel.state.collectAsStateWithLifecycle()
        val scope = rememberCoroutineScope()

        LaunchedEffect(viewmodel.actions) {
            viewmodel.actions.collect { action ->
                when (action) {
                    LoginError, RegistrationError -> println("error")
                    LoginSuccess, RegistrationSuccess -> scope.launch {
                        navigator.emitDestination(
                            NavigationEvent.Destination(
                                route = Destination.List,
                                builder = { popUpTo(Destination.Auth) { inclusive = true } },
                            ),
                        )
                    }
                }
            }
        }

        AuthScreen(
            state = state,
            onEvent = viewmodel::onEvent,
        )
    }
}

private fun NavGraphBuilder.List(
    navigator: Navigator,
) {
    composable<Destination.List> {
        val viewModel: NoteListViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(viewModel.actions) {
            viewModel.actions.collect { action ->
                when (action) {
                    is NoteListAction.NavigateToDetails -> navigator.emitDestination(
                        NavigationEvent.Destination(
                            route = Destination.Details(
                                noteId = action.noteId,
                                title = action.title,
                                text = action.text,
                            ),
                        ),
                    )
                    NoteListAction.NavigateToAuth -> navigator.emitDestination(
                        NavigationEvent.Destination(
                            route = Destination.Auth,
                            builder = { popUpTo(Destination.List) { inclusive = true } },
                        ),
                    )
                }
            }
        }

        CompositionLocalProvider(
            LocalAnimatedContentScope provides this,
        ) {
            ListScreen(
                state = state,
                onEvent = viewModel::onEvent,
            )
        }
    }
}

private fun NavGraphBuilder.Details(
    scope: CoroutineScope,
    navigator: Navigator,
) {
    composable<Destination.Details> {
        val viewModel: NoteDetailsViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(viewModel.actions) {
            viewModel.actions.collect { action ->
                when (action) {
                    NoteDetailsAction.NavigateToList -> navigator.emitDestination(
                        event = NavigationEvent.NavigateUp,
                    )
                }
            }
        }

        CompositionLocalProvider(
            LocalAnimatedContentScope provides this,
        ) {
            DetailsScreen(
                state = state,
                onEvent = viewModel::onEvent,
            )
        }
    }
}
