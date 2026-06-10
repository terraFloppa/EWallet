package com.example.finance5.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.finance5.NavRoutes
import com.example.finance5.data.entity.Transaction
import com.example.finance5.ui.state.item.CategoryItemUiState
import com.example.finance5.ui.viewmodel.CategoryViewModel
import com.example.finance5.ui.viewmodel.TransactionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionCreateScreen(
    navController: NavController,
    transactionViewModel: TransactionViewModel,
    categoryViewModel: CategoryViewModel,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    val transactionUiState by transactionViewModel.uiState.collectAsStateWithLifecycle()
    val categoryUiState by categoryViewModel.uiState.collectAsStateWithLifecycle()

    val categoryOptions = categoryUiState.categoryItemUiStateList

    if (categoryOptions.isEmpty()) {
//        scope.launch {
//            snackbarHostState.showSnackbar("Создайте сначала категорию")
//        }
        return
    }

    // Field inputs
    var amountInput by remember { mutableStateOf("") }
    var categoryInput by remember { mutableStateOf("") }

    // Actual values
    val amountValue = amountInput.toDoubleOrNull() ?: 0.0
    val selectedCategory = remember { mutableStateOf(categoryOptions[0]) }

    val isExpanded = remember { mutableStateOf(false) }

    // Date picker values
    val datePickerState = rememberDatePickerState()
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(40.dp, 0.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Добавьте новую транзакцию")
        // Сумма
        TextField(
            value = amountInput,
            onValueChange = { amountInput = it },
            placeholder = { Text("Введите сумму") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        // Выбор категории
        SelectCategoryButton(categoryOptions, selectedCategory, isExpanded)
        // Выбор даты
        Button(
            onClick = { showModal = true }
        ) {
            Text("Hit me!!")
        }
        if (showModal) {
            DatePickerModal(
                onDateSelected = { selectedDate = it },
                onDismiss = { showModal = false },
                datePickerState
            )
        }
        // Кнопка создания
        CreateButton(
            navController,
            scope,
            snackbarHostState,
            transactionViewModel,
            Transaction(
                amount = amountValue,
                categoryId = selectedCategory.value.id,
                date = datePickerState.getSelectedDate()
            )
        )
    }
}

@Composable
fun SelectCategoryButton(
    categoryOptions: List<CategoryItemUiState>,
    selectedCategory: MutableState<CategoryItemUiState>,
    isExpanded: MutableState<Boolean>
) {
    Box {
        TextButton(onClick = { isExpanded.value = !isExpanded.value }) {
            Text(text = selectedCategory.value.name)
        }
        DropdownMenu(
            expanded = isExpanded.value,
            onDismissRequest = { isExpanded.value = false },
            modifier = Modifier.size(200.dp)
        ) {
            categoryOptions.forEach {
                DropdownMenuItem(
                    text = { Text(it.name) },
                    onClick = {
                        selectedCategory.value = it
                        isExpanded.value = false
                    }
                )
            }
        }
    }
}

@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    datePickerState: DatePickerState
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun CreateButton(
    navController: NavController,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    transactionViewModel: TransactionViewModel,
    transaction: Transaction
) {
    TextButton(
        onClick = {
            if (transaction.amount > 0 && transaction.categoryId!! > 0) {
                transactionViewModel.insertTransaction(transaction)
                navController.navigate(NavRoutes.TransactionListScreen.route)
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