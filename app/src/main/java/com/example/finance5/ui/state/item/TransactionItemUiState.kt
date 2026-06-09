package com.example.finance5.ui.state.item

import java.time.LocalDate

data class TransactionItemUiState(
    val amount: Double,
    val categoryId: Int?,
    val date: LocalDate?,
    val id: Int
)