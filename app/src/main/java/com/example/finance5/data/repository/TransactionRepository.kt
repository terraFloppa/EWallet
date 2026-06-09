package com.example.finance5.data.repository

import com.example.finance5.data.datasource.ITransactionLocalDataSource
import com.example.finance5.data.entity.Transaction
import com.example.finance5.data.entity.TransactionWithCategory
import java.time.LocalDate

class TransactionRepository(
    val dataSource: ITransactionLocalDataSource
) : ITransactionRepository {
    override suspend fun fetchTransactions(): List<Transaction> {
        return dataSource.fetchTransactions()
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        dataSource.insertTransaction(transaction)
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        dataSource.updateTransaction(transaction)
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        dataSource.deleteTransaction(transaction)
    }

    override suspend fun fetchTransactionsWithCategories(): List<TransactionWithCategory> {
        return dataSource
            .fetchTransactionsWithCategories().sortedByDescending { it.transaction.date }
    }

    override suspend fun fetchTransactionsWithCategoriesFromPeriod(
        first: LocalDate,
        second: LocalDate
    ): List<TransactionWithCategory>? {
        return dataSource.fetchTransactionsWithCategoriesFromPeriod(first, second)
    }
}