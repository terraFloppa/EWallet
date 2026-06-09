package com.example.finance5.ui.state.list

import com.example.finance5.ui.state.item.CategoryItemUiState
import com.example.finance5.ui.state.item.TransactionItemUiState

data class TransactionListUiState(
    val transactionItems: List<TransactionItemUiState> = listOf(),
    val filter: Filter = Filter()
) {
    data class Filter(
        val categories: List<CategoryItemUiState> = listOf(),
        val enabled: Boolean = false
    )
}