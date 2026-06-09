package com.example.finance5.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance5.data.repository.ITransactionRepository
import com.example.finance5.ui.state.item.TransactionWithCategoryItemUiState
import com.example.finance5.ui.state.list.TransactionWithCategoryListUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class TransactionWithCategoryViewModel(
    private val repository: ITransactionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TransactionWithCategoryListUiState())
    val uiState: StateFlow<TransactionWithCategoryListUiState> = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    fun fetchTransactionsWithCategories() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val transactions = repository.fetchTransactionsWithCategories()

            _uiState.update { currentState ->
                currentState.copy(
                    transactionWithCategoryItems = transactions.map {
                        TransactionWithCategoryItemUiState(
                            it.transaction,
                            it.category
                        )
                    }
                )
            }
        }
    }

//    fun fetchTransactionsWithCategoriesFromPeriod(
//        first: LocalDate,
//        second: LocalDate
//    ) {
//        fetchJob?.cancel()
//        fetchJob = viewModelScope.launch {
//            val transactions = repository.fetchTransactionsWithCategoriesFromPeriod(first, second)
//
//            _uiState.update { currentState ->
//                currentState.copy(
//                    transactionWithCategoryItems = transactions?.map {
//                        TransactionWithCategoryItemUiState(
//                            it.transaction,
//                            it.category
//                        )
//                    } ?: listOf()
//                )
//            }
//        }
//    }
}