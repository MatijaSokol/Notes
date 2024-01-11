package com.matijasokol.presentation.list

import com.matijasokol.presentation.navigation.NavigationDestination

object ListDestination : NavigationDestination<Unit> {

  override fun route(): String = "list"
}
