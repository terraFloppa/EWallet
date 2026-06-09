package com.example.finance5.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance5.data.repository.ITransactionRepository
import com.example.finance5.ui.state.item.TransactionWithCategoryItemUiState
import com.example.finance5.ui.state.list.TransactionWithCategoryListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionWithCategoryViewModel(
    private val repository: ITransactionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TransactionWithCategoryListUiState())
    val uiState: StateFlow<TransactionWithCategoryListUiState> = _uiState.asStateFlow()

    init {
        observeTransactionsWithCategories()
    }

    private fun observeTransactionsWithCategories() {
        viewModelScope.launch {
            // Подписываемся на реактивный поток Flow из репозитория
            // Предполагается, что метод возвращает Flow<List<TransactionWithCategory>>
            repository.fetchTransactionsWithCategories().collect { transactions ->
                _uiState.update { currentState ->
                    currentState.copy(
                        transactionWithCategoryItems = transactions.map {
                            TransactionWithCategoryItemUiState(
                                transaction = it.transaction,
                                category = it.category
                            )
                        }
                    )
                }
            }
        }
    }
}
