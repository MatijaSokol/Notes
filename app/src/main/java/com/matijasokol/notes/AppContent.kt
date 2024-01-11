package com.matijasokol.notes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.matijasokol.notes.navigation.NavigationEffect
import com.matijasokol.notes.navigation.NavigationEvent
import com.matijasokol.notes.navigation.Navigator
import com.matijasokol.presentation.details.DetailsDestination
import com.matijasokol.presentation.details.DetailsScreen
import com.matijasokol.presentation.list.ListDestination
import com.matijasokol.presentation.list.ListScreen
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun AppContent() {
  val navController = rememberNavController()
  val navigator: Navigator = koinInject()
  val scope = rememberCoroutineScope()
  NavigationEffect(navController)

  NavHost(
    navController = navController,
    startDestination = ListDestination.route(),
  ) {
    listScreen {
      scope.launch {
        navigator.emitDestination(
          NavigationEvent.Destination(
            destination = DetailsDestination.buildRoute(20),
          ),
        )
      }
    }
    detailsScreen {
      scope.launch {
        navigator.emitDestination(
          NavigationEvent.NavigateUp,
        )
      }
    }
  }
}

private fun NavGraphBuilder.listScreen(
  onButtonClick: () -> Unit,
) {
  composable(
    route = ListDestination.route(),
  ) {
    ListScreen {
      onButtonClick()
    }
  }
}

private fun NavGraphBuilder.detailsScreen(
  onButtonClick: () -> Unit,
) {
  composable(
    route = DetailsDestination.route(),
    arguments = DetailsDestination.arguments,
  ) {
    DetailsScreen(
      param = it.arguments?.getInt("noteId") ?: error("Param is not provided"),
      onButtonClick = onButtonClick,
    )
  }
}
