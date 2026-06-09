package com.example.finance5.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.finance5.data.entity.Category
import com.example.finance5.data.entity.CategoryWithTransactions
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun fetchCategories() : Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE id == :id")
    suspend fun fetchCategoryById(id: Int) : Category

    @Insert
    suspend fun insertCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Transaction
    @Query("SELECT * FROM categories")
    fun fetchCategoriesWithTransactions(): Flow<List<CategoryWithTransactions>>
}