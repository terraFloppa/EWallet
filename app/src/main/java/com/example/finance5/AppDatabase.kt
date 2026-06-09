package com.example.finance5

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.finance5.data.dao.CategoryDao
import com.example.finance5.data.dao.TransactionDao
import com.example.finance5.data.entity.Category
import com.example.finance5.data.entity.Transaction

@Database(
    entities = [Category::class, Transaction::class],
    exportSchema = true,
    autoMigrations = [
        AutoMigration(1, 2),
        AutoMigration(2, 3)
                     ],
    version = 3
)
@TypeConverters(DateConverter::class) // Register here
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
}