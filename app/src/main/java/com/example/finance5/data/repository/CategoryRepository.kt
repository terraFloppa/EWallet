package com.example.finance5.data.repository

import com.example.finance5.data.entity.Category
import com.example.finance5.data.datasource.ICategoryLocalDataSource
import com.example.finance5.data.entity.CategoryWithTransactions
import kotlinx.coroutines.flow.Flow

class CategoryRepository(
    val dataSource: ICategoryLocalDataSource
) : ICategoryRepository {
    override fun fetchCategories(): Flow<List<Category>> {
        return dataSource.fetchCategories()
    }

    override suspend fun fetchCategoryById(id: Int): Category {
        return dataSource.fetchCategoryById(id)
    }

    override suspend fun insertCategory(category: Category) {
        dataSource.insertCategory(category)
    }

    override suspend fun updateCategory(category: Category) {
        dataSource.updateCategory(category)
    }

    override suspend fun deleteCategory(category: Category) {
        dataSource.deleteCategory(category)
    }

    override fun fetchCategoriesWithTransactions(): Flow<List<CategoryWithTransactions>> {
        return dataSource.fetchCategoriesWithTransactions()
    }
}