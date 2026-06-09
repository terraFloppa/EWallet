package com.example.finance5.ui.state.item

import com.example.finance5.data.entity.Category
import com.example.finance5.data.entity.Transaction

data class TransactionWithCategoryItemUiState(
    val transaction: Transaction,
    val category: Category?
)
