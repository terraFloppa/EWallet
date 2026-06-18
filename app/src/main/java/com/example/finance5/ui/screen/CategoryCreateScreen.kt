package com.example.finance5.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finance5.CategoryType
import com.example.finance5.NavRoutes
import com.example.finance5.SelectTypeButton
import com.example.finance5.data.entity.Category
import com.example.finance5.ui.viewmodel.CategoryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CategoryCreateScreen(
    navController: NavController,
    categoryViewModel: CategoryViewModel,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    var nameInput by remember { mutableStateOf("") }
    var typeInput by remember { mutableStateOf(CategoryType.EXPENSE) }

    Column {
        Text("Добавьте новую категорию")
        // Название
        TextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            placeholder = { Text("Введите название") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        // Тип категории
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            val underlinedButtonModifier = Modifier.weight(10f)

            SelectTypeButton(underlinedButtonModifier, typeInput, CategoryType.EXPENSE) {
                typeInput = CategoryType.EXPENSE
            }
            SelectTypeButton(underlinedButtonModifier, typeInput, CategoryType.INCOME) {
                typeInput = CategoryType.INCOME
            }
        }
        // Кнопка создания
        TextButton(
            onClick = {
                if (nameInput != "") {
                    categoryViewModel.insertCategory(
                        Category(
                            name = nameInput,
                            type = typeInput
                        ),
                    )
                    navController.navigate(NavRoutes.CategoryListScreen.route)
                }
                else {
                    scope.launch {
                        snackbarHostState.showSnackbar("Введите все значения!")
                    }
                }
            }
        ) {
            Text("Создать")
        }
    }
}

@Composable
fun SwitchCategoryTypeRow() {

}
