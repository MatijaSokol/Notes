package com.matijasokol.notes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.matijasokol.notes.details.DetailsScreen
import com.matijasokol.notes.list.ListScreen
import com.matijasokol.notes.navigation.Destination
import com.matijasokol.notes.navigation.NavigationEffect
import com.matijasokol.notes.navigation.NavigationEvent
import com.matijasokol.notes.navigation.Navigator
import com.matijasokol.notes.ui.theme.NotesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
fun AppContent() {
  KoinContext {
    NotesTheme {
      val navController = rememberNavController()
      val navigator: Navigator = koinInject()
      val scope = rememberCoroutineScope()

      NavigationEffect(navController)

      NavHost(
        navController = navController,
        startDestination = Destination.List,
      ) {
        List(scope, navigator)
        Details(scope, navigator)
      }
    }
  }
}

private fun NavGraphBuilder.List(
  scope: CoroutineScope,
  navigator: Navigator,
) {
  composable<Destination.List> {
    ListScreen {
      scope.launch {
        navigator.emitDestination(
          NavigationEvent.Destination(
            route = Destination.Details(noteId = 20),
          ),
        )
      }
    }
  }
}

private fun NavGraphBuilder.Details(
  scope: CoroutineScope,
  navigator: Navigator,
) {
  composable<Destination.Details> {
    DetailsScreen(
      param = it.toRoute<Destination.Details>().noteId,
      onButtonClick = {
        scope.launch {
          navigator.emitDestination(
            NavigationEvent.NavigateUp,
          )
        }
      },
    )
  }
}
