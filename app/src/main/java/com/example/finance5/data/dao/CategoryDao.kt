package com.example.finance5.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.finance5.data.entity.Category
import com.example.finance5.data.entity.CategoryWithTransactions

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun fetchCategories() : List<Category>

    @Query("SELECT * FROM categories WHERE id == :id")
    fun fetchCategoryById(id: Int) : Category

    @Insert
    fun insertCategory(category: Category)

    @Update
    fun updateCategory(category: Category)

    @Delete
    fun deleteCategory(category: Category)

    @Transaction
    @Query("SELECT * FROM categories")
    fun fetchCategoriesWithTransactions(): List<CategoryWithTransactions>
}