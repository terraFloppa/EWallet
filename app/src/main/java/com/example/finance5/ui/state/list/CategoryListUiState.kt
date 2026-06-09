package com.example.finance5.ui.state.list

import com.example.finance5.CategoryType
import com.example.finance5.ui.state.item.CategoryItemUiState

data class CategoryListUiState (
    val categoryItemUiStateList: List<CategoryItemUiState> = listOf()
)