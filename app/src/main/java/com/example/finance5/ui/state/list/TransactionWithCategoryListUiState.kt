package com.example.finance5.ui.state.list

import com.example.finance5.ui.state.item.TransactionWithCategoryItemUiState
import java.time.LocalDate

data class TransactionWithCategoryListUiState(
    val transactionWithCategoryItems: List<TransactionWithCategoryItemUiState> = listOf(),
)
