package com.example.finance5.data.datasource

import com.example.finance5.data.entity.Transaction
import com.example.finance5.data.entity.TransactionWithCategory
import java.time.LocalDate

interface ITransactionLocalDataSource {
    suspend fun fetchTransactions() : List<Transaction>

    suspend fun insertTransaction(transaction: Transaction)

    suspend fun updateTransaction(transaction: Transaction)

    suspend fun deleteTransaction(transaction: Transaction)

    suspend fun fetchTransactionsWithCategories(): List<TransactionWithCategory>

    suspend fun fetchTransactionsWithCategoriesFromPeriod(first: LocalDate, second: LocalDate): List<TransactionWithCategory>?
}