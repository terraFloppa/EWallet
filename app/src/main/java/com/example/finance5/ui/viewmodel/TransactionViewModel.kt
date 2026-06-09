package com.example.finance5.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance5.data.entity.Transaction
import com.example.finance5.ui.state.item.TransactionItemUiState
import com.example.finance5.ui.state.list.TransactionListUiState
import com.example.finance5.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val repository: TransactionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TransactionListUiState())
    val uiState: StateFlow<TransactionListUiState> = _uiState.asStateFlow()

    init {
        // Запускаем постоянное наблюдение за базой данных при создании ViewModel
        observeTransactions()
    }

    private fun observeTransactions() {
        viewModelScope.launch {
            // Предполагается, что вы изменили метод в репозитории на возвращающий Flow<List<Transaction>>
            repository.fetchTransactions().collect { transactions ->
                _uiState.update { currentState ->
                    currentState.copy(
                        transactionItems = transactions.map {
                            TransactionItemUiState(
                                amount = it.amount,
                                categoryId = it.categoryId,
                                date = it.date,
                                id = it.id
                            )
                        }
                    )
                }
            }
        }
    }

    fun insertTransaction(transaction: Transaction) {
        // Запускаем операцию в отдельной изолированной корутине без отмены других процессов
        viewModelScope.launch {
            repository.insertTransaction(transaction)
            // fetchTransactions() БОЛЬШЕ ВЫЗЫВАТЬ НЕ НУЖНО! Room сам обновит Flow
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.updateTransaction(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }
}
