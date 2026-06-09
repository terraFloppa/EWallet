package com.example.finance5.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.finance5.NavRoutes
import com.example.finance5.data.entity.Transaction
import com.example.finance5.ui.viewmodel.CategoryViewModel
import com.example.finance5.ui.viewmodel.TransactionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionEditScreen(
    navController: NavController,
    transactionViewModel: TransactionViewModel,
    categoryViewModel: CategoryViewModel,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    id: Int?
) {
    val transactions by transactionViewModel.uiState.collectAsStateWithLifecycle()
    transactionViewModel.fetchTransactions()

    val categories by categoryViewModel.uiState.collectAsStateWithLifecycle()
    categoryViewModel.fetchCategories()

    val transaction = transactions.transactionItems.find { it.id == id }

    if (transaction == null)
        return

    // Field inputs
    var amountState by remember { mutableStateOf(transaction.amount.toString()) }
    var categoryState by remember { mutableStateOf(transaction.categoryId.toString()) }

    // Date picker values
    val datePickerState = rememberDatePickerState()
    val dateFormatter = remember { DatePickerDefaults.dateFormatter() }

    // Dropdown vars
    var isExpanded by remember { mutableStateOf(false) }

    // Button Values
    val amountValue = amountState.toDoubleOrNull() ?: 0.0
    val categoryValue = categoryState.toIntOrNull() ?: 0

    Column {
        Text("Измените транзакцию")
        // Сумма
        TextField(
            value = amountState,
            onValueChange = { amountState = it },
            placeholder = { Text("Введите сумму") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        // Категория
        Box (
        ) {
            Button (onClick = { isExpanded = !isExpanded }) {
                Text(text = "Категория")
            }
            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier.size(200.dp)
            ) {
                for (c in categories.categoryItemUiStateList) {
                    DropdownMenuItem(
                        onClick = {
                            categoryState = c.id.toString()
                            isExpanded = false
                        },
                        text = { Text(c.name) }
                    )
                }
            }
        }
        // Выбор даты
        DatePicker(
            state = datePickerState,
            dateFormatter = dateFormatter,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(
                    scaleX = 1f,
                    scaleY = 1f,
                    transformOrigin = TransformOrigin(0f, 0f)
                )
        )
        // Кнопки
        Row {
            // Кнопка сохранения
            TextButton(
                onClick = {
                    if (amountValue > 0 && categoryValue > 0) {
                        transactionViewModel.updateTransaction(
                            Transaction(
                                amount = amountValue,
                                categoryId = categoryValue,
                                date = datePickerState.getSelectedDate()
                            ),
                        )
                        navController.navigate(NavRoutes.TransactionListScreen.route)
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
                    if (amountValue > 0 && categoryValue > 0) {
                        transactionViewModel.deleteTransaction(
                            Transaction(
                                amount = amountValue,
                                categoryId = categoryValue,
                                date = datePickerState.getSelectedDate()
                            ),
                        )
                        navController.navigate(NavRoutes.TransactionListScreen.route)
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