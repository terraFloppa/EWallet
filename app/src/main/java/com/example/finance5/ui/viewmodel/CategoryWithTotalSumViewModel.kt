package com.example.finance5.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance5.CategoryType
import com.example.finance5.data.repository.ICategoryRepository
import com.example.finance5.ui.state.item.CategoryWithTotalSumItemUiState
import com.example.finance5.ui.state.list.CategoryWithTotalSumListUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryWithTotalSumViewModel(
    private val repository: ICategoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoryWithTotalSumListUiState())
    val uiState: StateFlow<CategoryWithTotalSumListUiState> = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    fun fetchCategoryWithTotalSum() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val list = repository.fetchCategoriesWithTransactions()

            // Конвертируем CategoryWithTransactions в CategoryWithTotalSumItemUiState
            // (подсчитываем сумму транзакций для каждой категории)
            _uiState.update { currentState ->
                currentState.copy(
                    categoryWithTotalSumItems = list.map { it ->
                        CategoryWithTotalSumItemUiState(
                            category = it.category,
                            totalSum = it.transactions.sumOf { it.amount }
                        )
                    }.sortedByDescending { it.totalSum }
                )
            }
        }
    }

    fun setFilter(type: CategoryType) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    chosenType = type
                )
            }
        }
    }
}