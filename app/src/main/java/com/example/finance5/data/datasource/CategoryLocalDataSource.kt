package com.example.finance5.data.datasource

import com.example.finance5.data.dao.CategoryDao
import com.example.finance5.data.entity.Category
import com.example.finance5.data.entity.CategoryWithTransactions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class CategoryLocalDataSource (
    private val categoryDao: CategoryDao,
) : ICategoryLocalDataSource {
    override suspend fun fetchCategories(): List<Category> {
        return categoryDao.fetchCategories()
    }

    override fun fetchCategoryById(id: Int): Category {
        return categoryDao.fetchCategoryById(id)
    }

    override suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category)
    }

    override suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category)
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }

    override suspend fun fetchCategoriesWithTransactions(): List<CategoryWithTransactions> {
        return categoryDao.fetchCategoriesWithTransactions()
    }
}
