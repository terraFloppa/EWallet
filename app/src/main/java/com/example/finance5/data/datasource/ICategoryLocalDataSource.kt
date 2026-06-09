package com.example.finance5.data.datasource

import com.example.finance5.data.entity.Category
import com.example.finance5.data.entity.CategoryWithTransactions

interface ICategoryLocalDataSource {
    suspend fun fetchCategories() : List<Category>

    fun fetchCategoryById(id: Int) : Category

    suspend fun insertCategory(category: Category)

    suspend fun updateCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    suspend fun fetchCategoriesWithTransactions(): List<CategoryWithTransactions>
}