package com.example.finance5

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlin.collections.contains

@Composable
fun NavBar(navController: NavController, currentRoute: String?) {
    if (currentRoute !in
        listOf(NavRoutes.TransactionListScreen.route, NavRoutes.CategoryListScreen.route))
        return

    NavigationBar {
//        val backStackEntry by navController.currentBackStackEntryAsState()
//        val currentRoute = backStackEntry?.destination?.route

        NavBarItems.barItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        // TODO: ХЗ ЧЕ ЭТО? !!!!!!!!!!
                        popUpTo(navController.graph.findStartDestination().id) {saveState = true}
                        launchSingleTop = true
                        restoreState = true
                        // ХЗ ЧЕ ЭТО? !!!!!!!!!!
                    }
                },
                icon = {},
                label = { Text(navItem.title) }
            )
        }
    }
}

object NavBarItems {
    val barItems = listOf(
        BarItem("Транзакции", NavRoutes.TransactionListScreen.route),
        BarItem("Категории", NavRoutes.CategoryListScreen.route)
    )
}

data class BarItem(
    val title: String,
    val route: String
)