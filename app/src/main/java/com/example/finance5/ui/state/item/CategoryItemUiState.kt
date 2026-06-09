package com.example.finance5.ui.state.item

import com.example.finance5.CategoryType

data class CategoryItemUiState (
    val name: String,
    val type: CategoryType?,
    val id: Int
)