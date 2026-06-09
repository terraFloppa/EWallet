package com.example.finance5.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance5.data.entity.Category
import com.example.finance5.data.repository.ICategoryRepository
import com.example.finance5.ui.state.item.CategoryItemUiState
import com.example.finance5.ui.state.list.CategoryListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val repository: ICategoryRepository
) : ViewModel() {
    // Состояние UI экрана категорий (доступно для чтения из Compose)
    private val _uiState = MutableStateFlow(CategoryListUiState())
    val uiState: StateFlow<CategoryListUiState> = _uiState.asStateFlow()

    init {
        // Начинаем наблюдение за категориями сразу при создании ViewModel
        observeCategories()
    }

    private fun observeCategories() {
        viewModelScope.launch {
            // Подписываемся на Flow поток изменений из репозитория
            repository.fetchCategories().collect { categories ->
                _uiState.update { currentState ->
                    currentState.copy(
                        categoryItemUiStateList = categories.map {
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

    fun insertCategory(category: Category) {
        viewModelScope.launch {
            repository.insertCategory(category)
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            repository.updateCategory(category)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            repository.deleteCategory(category)
        }
    }
}
