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
    override fun fetchCategories(): Flow<List<Category>> {
        return categoryDao.fetchCategories()
    }

    override suspend fun fetchCategoryById(id: Int): Category {
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

    override fun fetchCategoriesWithTransactions(): Flow<List<CategoryWithTransactions>> {
        return categoryDao.fetchCategoriesWithTransactions()
    }
}
