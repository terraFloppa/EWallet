package com.example.finance5.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance5.data.repository.ICategoryRepository
import com.example.finance5.ui.state.FilterUiState
import com.example.finance5.ui.state.item.CategoryItemUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class FilterViewModel(
    private val repository: ICategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState: StateFlow<FilterUiState> = _uiState.asStateFlow()

    init {
        // Автоматически подписываемся на категории из базы данных при старте
        observeCategoriesForFilter()
    }

    private fun observeCategoriesForFilter() {
        viewModelScope.launch {
            // Подписываемся на поток категорий из БД
            repository.fetchCategories().collect { categories ->
                _uiState.update { currentState ->
                    currentState.copy(
                        selectedCategories = categories.map {
                            CategoryItemUiState(
                                name = it.name,
                                type = it.type,
                                id = it.id
                            )
                        }
                    )
                }
            }
        }
    }

    // Если нужно принудительно обновить список категорий из UI (без участия БД)
    fun updateSelectedCategories(categories: List<CategoryItemUiState>) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedCategories = categories
            )
        }
    }

    // Изменение периода вперед (на 1 месяц) — корутины и Job больше не нужны
    fun increase() {
        _uiState.update { currentState ->
            val currentPeriod = currentState.selectedPeriod
            currentState.copy(
                // plusMonths(1) автоматически правильно переведет декабрь на январь следующего года
                selectedPeriod = currentPeriod.plusMonths(1)
            )
        }
    }

    // Изменение периода назад (на 1 месяц)
    fun decrease() {
        _uiState.update { currentState ->
            val currentPeriod = currentState.selectedPeriod
            currentState.copy(
                // minusMonths(1) автоматически правильно переведет январь на декабрь предыдущего года
                selectedPeriod = currentPeriod.minusMonths(1)
            )
        }
    }
}
