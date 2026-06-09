package com.example.finance5.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithTransactions(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val transactions: List<Transaction>
)
