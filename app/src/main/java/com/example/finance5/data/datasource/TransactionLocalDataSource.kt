package com.example.finance5.data.datasource

import com.example.finance5.data.dao.TransactionDao
import com.example.finance5.data.entity.Transaction
import com.example.finance5.data.entity.TransactionWithCategory
import java.time.LocalDate

class TransactionLocalDataSource(
    private val transactionDao: TransactionDao
) : ITransactionLocalDataSource {
    override suspend fun fetchTransactions(): List<Transaction> {
        return transactionDao.fetchTransactions()
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }

    override suspend fun fetchTransactionsWithCategories(): List<TransactionWithCategory> {
        return transactionDao.fetchTransactionsWithCategories()
    }

    override suspend fun fetchTransactionsWithCategoriesFromPeriod(
        first: LocalDate,
        second: LocalDate
    ): List<TransactionWithCategory>? {
        return transactionDao.fetchTransactionsWithCategoriesFromPeriod(first, second)
    }
}