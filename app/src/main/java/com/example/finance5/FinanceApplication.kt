package com.example.finance5

import android.app.Application
import androidx.room.Room
import com.example.finance5.data.dao.CategoryDao
import com.example.finance5.data.dao.TransactionDao
import com.example.finance5.data.datasource.CategoryLocalDataSource
import com.example.finance5.data.datasource.TransactionLocalDataSource
import com.example.finance5.data.repository.CategoryRepository
import com.example.finance5.data.repository.TransactionRepository

class FinanceApplication : Application() {
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "finance5_db"
        )
            // .fallbackToDestructiveMigration() // Полезно при разработке, если меняется схема БД
            .build()
    }

    val transactionDao: TransactionDao by lazy {
        database.transactionDao()
    }

    val categoryDao: CategoryDao by lazy {
        database.categoryDao()
    }

    val transactionLocalDataSource: TransactionLocalDataSource by lazy {
        TransactionLocalDataSource(transactionDao)
    }

    val categoryLocalDataSource: CategoryLocalDataSource by lazy {
        CategoryLocalDataSource(categoryDao)
    }

    val transactionRepository: TransactionRepository by lazy {
        TransactionRepository(transactionLocalDataSource)
    }

    val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(categoryLocalDataSource)
    }
}