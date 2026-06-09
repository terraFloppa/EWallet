package com.example.finance5.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.finance5.CategoryType
import com.example.finance5.NavRoutes
import com.example.finance5.data.entity.Category
import com.example.finance5.ui.viewmodel.CategoryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CategoryEditScreen(
    navController: NavController,
    viewModel: CategoryViewModel,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    id: Int?
) {
    val categoryUiState by viewModel.uiState.collectAsStateWithLifecycle()

    val category = categoryUiState.categoryItemUiStateList.find { it.id == id }

    if (category == null)
        return

    var nameInput by remember { mutableStateOf(category.name) }
    var typeInput by remember { mutableStateOf(category.type) }

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
        Row {
            Button(
                onClick = {
                    typeInput = CategoryType.INCOME
                }
            ) {
                Text("Доход")
            }
            Button(
                onClick = {
                    typeInput = CategoryType.EXPENSE
                }
            ) {
                Text("Расход")
            }
        }
        // Кнопки
        Row {
            // Кнопка сохранения
            TextButton(
                onClick = {
                    if (nameInput != "") {
                        viewModel.updateCategory(
                            Category(
                                name = nameInput,
                                type = typeInput,
                                id = id!!
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
                Text("Сохранить")
            }
            // Кнопка удаления
            TextButton(
                onClick = {
                    if (nameInput != "") {
                        viewModel.deleteCategory(
                            Category(
                                name = nameInput,
                                type = typeInput,
                                id = id!!
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
                Text("Удалить")
            }
        }
    }
}