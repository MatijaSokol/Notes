package com.matijasokol.notes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
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
    startDestination = ListDestination,
  ) {
    composable<ListDestination> {
      ListScreen {
        scope.launch {
          navigator.emitDestination(
            NavigationEvent.Destination(
              route = DetailsDestination(noteId = 20),
            ),
          )
        }
      }
    }

    composable<DetailsDestination> {
      DetailsScreen(
        param = it.toRoute<DetailsDestination>().noteId,
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
}
