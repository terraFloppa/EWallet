package com.example.finance5.data.repository

import com.example.finance5.data.entity.Category
import com.example.finance5.data.entity.CategoryWithTransactions
import kotlinx.coroutines.flow.Flow

interface ICategoryRepository {
    fun fetchCategories() : Flow<List<Category>>

    suspend fun fetchCategoryById(id: Int) : Category

    suspend fun insertCategory(category: Category)

    suspend fun updateCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    fun fetchCategoriesWithTransactions(): Flow<List<CategoryWithTransactions>>
}