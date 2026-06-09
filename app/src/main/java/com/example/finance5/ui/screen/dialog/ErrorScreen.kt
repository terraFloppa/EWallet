package com.example.finance5.ui.screen.dialog

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ErrorScreen() {
    Box(
        //modifier = Modifier.background(Color.WHITE),
        contentAlignment = Alignment.Center
    ) {
        Text("Введите все значения!")
    }
}