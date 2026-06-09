package com.example.finance5.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.finance5.CategoryType

@Entity(tableName = "categories")
data class Category(
    val name: String,
    @ColumnInfo(name = "category_type") val type: CategoryType?,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)