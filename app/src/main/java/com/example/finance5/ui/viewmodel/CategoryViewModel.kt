package com.example.finance5.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance5.data.entity.Category
import com.example.finance5.data.repository.ICategoryRepository
import com.example.finance5.ui.state.item.CategoryItemUiState
import com.example.finance5.ui.state.list.CategoryListUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val repository: ICategoryRepository
) : ViewModel() {
    // Expose screen UI state
    private val _uiState = MutableStateFlow(CategoryListUiState())
    val uiState: StateFlow<CategoryListUiState> = _uiState.asStateFlow()

    // TODO WHAT is JOB, privateset ViewModel
    private var fetchJob: Job? = null

    fun fetchCategories() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
           val categories = repository.fetchCategories()

            _uiState.update { currentState ->
                currentState.copy(
                    categoryItemUiStateList = categories.map {
                        CategoryItemUiState(
                            it.name,
                            it.type,
                            it.id
                        )
                    }
                )
            }
        }
    }

//    fun fetchCategoryById(id: Int) : CategoryItemUiState {
//        fetchJob?.cancel()
//        fetchJob = viewModelScope.launch {
//
//        }
//    }

    fun insertCategory(category: Category) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            repository.insertCategory(category)
        }
        fetchCategories()
    }

    fun updateCategory(category: Category) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            repository.updateCategory(category)
        }
        fetchCategories()
    }

    fun deleteCategory(category: Category) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            repository.deleteCategory(category)
        }
        fetchCategories()
    }
}