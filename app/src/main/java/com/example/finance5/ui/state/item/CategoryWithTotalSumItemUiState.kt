package com.example.finance5.ui.state.item

import com.example.finance5.data.entity.Category

data class CategoryWithTotalSumItemUiState(
    val category: Category,
    val totalSum: Double
)