package com.example.finance5.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate


// TODO DATE!!!
@Entity(tableName = "transactions")
data class Transaction(
    val amount: Double,
    @ColumnInfo(name = "category_id") val categoryId: Int?,
    val date: LocalDate? = LocalDate.now(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
