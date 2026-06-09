package com.example.finance5.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance5.CategoryType
import com.example.finance5.data.repository.ICategoryRepository
import com.example.finance5.ui.state.FilterUiState
import com.example.finance5.ui.state.item.CategoryItemUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class FilterViewModel (
    private val repository: ICategoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState: StateFlow<FilterUiState> = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    fun updateSelectedCategories() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val categories = repository.fetchCategories()
            _uiState.update { currentState ->
                currentState.copy(
                    selectedCategories = categories.map {
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

    fun updateSelectedCategories(categories: List<CategoryItemUiState>) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    selectedCategories = categories
                )
            }
        }
    }

    fun increase() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val vv = _uiState.value.selectedPeriod

            _uiState.update { currentState ->
                currentState.copy(
                    selectedPeriod = LocalDate.of(
                        vv.year,
                        vv.month + 1,
                        vv.dayOfMonth
                    )
                )
            }
        }
    }

    fun decrease() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val vv = _uiState.value.selectedPeriod

            _uiState.update { currentState ->
                currentState.copy(
                    selectedPeriod = LocalDate.of(
                        vv.year,
                        vv.month - 1,
                        vv.dayOfMonth
                    )
                )
            }
        }
    }


}