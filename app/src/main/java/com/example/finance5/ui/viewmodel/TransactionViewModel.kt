package com.example.finance5.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance5.data.entity.Transaction
import com.example.finance5.ui.state.item.TransactionItemUiState
import com.example.finance5.ui.state.list.TransactionListUiState
import com.example.finance5.data.repository.TransactionRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionViewModel(
    val repository: TransactionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TransactionListUiState())
    val uiState: StateFlow<TransactionListUiState> = _uiState.asStateFlow()

    // TODO WHAT is JOB, privateset ViewModel
    private var fetchJob: Job? = null

    fun fetchTransactions() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val transactions = repository.fetchTransactions()

            _uiState.update { currentState ->
                currentState.copy(
                    transactionItems = transactions.map {
                        TransactionItemUiState(
                            it.amount,
                            it.categoryId,
                            it.date,
                            it.id
                        )
                    }
                )
            }
        }
    }

    fun insertTransaction(transaction: Transaction) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            repository.insertTransaction(transaction)
        }
        fetchTransactions()
    }

    fun updateTransaction(transaction: Transaction) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            repository.updateTransaction(transaction)
        }
        fetchTransactions()
    }

    fun deleteTransaction(transaction: Transaction) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
        fetchTransactions()
    }
}