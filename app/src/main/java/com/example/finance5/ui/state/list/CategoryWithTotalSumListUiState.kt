package com.example.finance5.ui.state.list

import com.example.finance5.CategoryType
import com.example.finance5.ui.state.item.CategoryWithTotalSumItemUiState
import java.time.LocalDate

data class CategoryWithTotalSumListUiState(
    val categoryWithTotalSumItems: List<CategoryWithTotalSumItemUiState> = listOf(),
    val chosenType: CategoryType = CategoryType.EXPENSE,
    //val chosenMonth: LocalDate = LocalDate.now().
)