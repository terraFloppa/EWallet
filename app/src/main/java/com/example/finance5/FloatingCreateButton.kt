package com.example.finance5

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController

@Composable
fun FloatingCreateButton(
    navController: NavController,
    currentRoute: String?
) {
    val destRoute = when (currentRoute) {
        NavRoutes.TransactionListScreen.route ->
            NavRoutes.TransactionCreateScreen.route
        NavRoutes.CategoryListScreen.route ->
            NavRoutes.CategoryCreateScreen.route
        else -> return
    }

    FloatingActionButton(
        onClick = { navController.navigate(destRoute) },
        shape = CircleShape
    ) {
        Icon(painterResource(R.drawable.ic_add_icon), contentDescription = "Создать")
    }
}