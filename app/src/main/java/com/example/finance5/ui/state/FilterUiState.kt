package com.example.finance5.ui.state

import com.example.finance5.CategoryType
import com.example.finance5.data.entity.Category
import com.example.finance5.ui.state.item.CategoryItemUiState
import java.time.LocalDate

data class FilterUiState(
    val selectedPeriod: LocalDate = LocalDate.now(),
    val selectedCategories: List<CategoryItemUiState> = listOf(),
)
