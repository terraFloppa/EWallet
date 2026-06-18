package com.example.finance5.ui.screen

import androidx.collection.emptyLongSet
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerState
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
import androidx.compose.ui.text.input.KeyboardType
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
    val transactionUiState by transactionViewModel.uiState.collectAsStateWithLifecycle()
    val categoryUiState by categoryViewModel.uiState.collectAsStateWithLifecycle()

    val transaction = transactionUiState.transactionItems.find { it.id == id }
    val categoryOptions = categoryUiState.categoryItemUiStateList

    val selectedCategory = remember { mutableStateOf(categoryOptions[0]) }
    val isExpanded = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showModal by remember { mutableStateOf(false) }

    if (transaction == null)
        return

    // Field inputs
    var amountState by remember { mutableStateOf(transaction.amount.toString()) }
    var categoryState by remember { mutableStateOf(transaction.categoryId.toString()) }

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
        // Выбор категории
        SelectCategoryButton(categoryOptions, selectedCategory, isExpanded)
        // Выбор даты
        Button(
            onClick = { showModal = true }
        ) {
            if (selectedDate == null) {
                Text("Введите дату")
            }
            else {
                Text(selectedDate.toString())
            }
        }
        if (showModal) {
            DatePickerModal(
                onDateSelected = { selectedDate = it },
                onDismiss = { showModal = false },
                datePickerState
            )
        }
        // Кнопки
        Row {
            // Кнопка сохранения
            SaveButton(
                amountValue, categoryValue, transactionViewModel, datePickerState, id,
                navController, scope, snackbarHostState
            )
            // Кнопка удаления
            DeleteButton(amountValue, categoryValue, transactionViewModel, datePickerState, id,
                navController, scope, snackbarHostState
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteButton(
    amountValue: Double,
    categoryValue: Int,
    transactionViewModel: TransactionViewModel,
    datePickerState: DatePickerState,
    id: Int?,
    navController: NavController,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    TextButton(
        onClick = {
            transactionViewModel.deleteTransaction(
                Transaction(
                    amount = 0.0,
                    categoryId = 0,
                    date = null,
                    id = id!!
                ),
            )
            navController.navigate(NavRoutes.TransactionListScreen.route)
        }
    ) {
        Text("Удалить")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveButton(
    amountValue: Double,
    categoryValue: Int,
    transactionViewModel: TransactionViewModel,
    datePickerState: DatePickerState,
    id: Int?,
    navController: NavController,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    TextButton(
        onClick = {
            if (amountValue > 0 && categoryValue > 0) {
                transactionViewModel.updateTransaction(
                    Transaction(
                        amount = amountValue,
                        categoryId = categoryValue,
                        date = datePickerState.getSelectedDate(),
                        id = id!!
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
}