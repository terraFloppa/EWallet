package com.example.finance5

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun <T> CreateEditScreenButton(
    navController: NavController,
    navRoute: String,
    message: String,
    entity: T,
    oper: (T) -> Unit
) {
    Text(
        modifier = Modifier
            .clickable(
                enabled = true,
                onClick = {
                    oper(entity)
                    navController.navigate(navRoute)
                }
            ),
        text = message
    )
}