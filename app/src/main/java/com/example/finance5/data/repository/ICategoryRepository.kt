package com.example.finance5.data.repository

import com.example.finance5.data.entity.Category
import com.example.finance5.data.entity.CategoryWithTransactions

interface ICategoryRepository {
    suspend fun fetchCategories() : List<Category>

    suspend fun fetchCategoryById(id: Int) : Category

    suspend fun insertCategory(category: Category)

    suspend fun updateCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    suspend fun fetchCategoriesWithTransactions(): List<CategoryWithTransactions>
}