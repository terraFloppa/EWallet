package com.example.finance5.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance5.CategoryType
import com.example.finance5.data.repository.ICategoryRepository
import com.example.finance5.ui.state.item.CategoryWithTotalSumItemUiState
import com.example.finance5.ui.state.list.CategoryWithTotalSumListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryWithTotalSumViewModel(
    private val repository: ICategoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoryWithTotalSumListUiState())
    val uiState: StateFlow<CategoryWithTotalSumListUiState> = _uiState.asStateFlow()

    // Внутренний поток для отслеживания выбранного пользователем фильтра
    private val _chosenType = MutableStateFlow<CategoryType?>(null)

    init {
        observeCategoriesWithTotalSum()
    }

    private fun observeCategoriesWithTotalSum() {
        viewModelScope.launch {
            // Объединяем реактивный поток из репозитория и поток выбранного фильтра
            // Предполагается, что метод возвращает Flow<List<CategoryWithTransactions>>
            combine(
                repository.fetchCategoriesWithTransactions(),
                _chosenType
            ) { dbList, filterType ->

                // 1. Фильтруем список из БД по выбранному типу (если фильтр установлен)
                val filteredList = if (filterType != null) {
                    dbList.filter { it.category.type == filterType }
                } else {
                    dbList
                }

                // 2. Маппим и подсчитываем сумму транзакций
                val uiItems = filteredList.map { item ->
                    CategoryWithTotalSumItemUiState(
                        category = item.category,
                        totalSum = item.transactions.sumOf { it.amount }
                    )
                }.sortedByDescending { it.totalSum } // Сортируем от больших трат к меньшим

                // Возвращаем обновленный стейт для копирования
                Pair(uiItems, filterType)
            }.collect { (calculatedItems, currentFilter) ->
                // Обновляем единый UiState экрана
                _uiState.update { currentState ->
                    currentState.copy(
                        categoryWithTotalSumItems = calculatedItems,
                        chosenType = currentFilter ?: currentState.chosenType // Сохраняем или обновляем тип
                    )
                }
            }
        }
    }

    // Изменение фильтра теперь происходит мгновенно и безопасно без перезапуска Job
    fun setFilter(type: CategoryType) {
        _chosenType.value = type
    }
}
