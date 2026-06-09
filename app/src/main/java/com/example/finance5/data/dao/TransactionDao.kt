package com.example.finance5.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction as _Transaction
import androidx.room.Update
import com.example.finance5.data.entity.Transaction
import com.example.finance5.data.entity.TransactionWithCategory
import java.time.LocalDate

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions")
    fun fetchTransactions() : List<Transaction>

    @Insert
    fun insertTransaction(transaction: Transaction)

    @Update
    fun updateTransaction(transaction: Transaction)

    @Delete
    fun deleteTransaction(transaction: Transaction)

    @_Transaction
    @Query("SELECT * FROM transactions")
    fun fetchTransactionsWithCategories(): List<TransactionWithCategory>

    @_Transaction
    @Query("SELECT * FROM transactions WHERE (date > :first & date < :second)")
    fun fetchTransactionsWithCategoriesFromPeriod(first: LocalDate, second: LocalDate): List<TransactionWithCategory>?
}