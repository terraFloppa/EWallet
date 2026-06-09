package com.example.finance5.data.repository

import com.example.finance5.data.datasource.ITransactionLocalDataSource
import com.example.finance5.data.entity.Transaction
import com.example.finance5.data.entity.TransactionWithCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDate
import kotlinx.coroutines.flow.flowOn

class TransactionRepository(
    val dataSource: ITransactionLocalDataSource
) : ITransactionRepository {
    override fun fetchTransactions(): Flow<List<Transaction>> {
        return dataSource.fetchTransactions().flowOn(Dispatchers.IO)
    }

    override suspend fun insertTransaction(transaction: Transaction) = withContext(Dispatchers.IO) {
        dataSource.insertTransaction(transaction)
    }

    override suspend fun updateTransaction(transaction: Transaction) = withContext(Dispatchers.IO) {
        dataSource.updateTransaction(transaction)
    }

    override suspend fun deleteTransaction(transaction: Transaction) = withContext(Dispatchers.IO) {
        dataSource.deleteTransaction(transaction)
    }

    override fun fetchTransactionsWithCategories(): Flow<List<TransactionWithCategory>> {
        return dataSource.fetchTransactionsWithCategories().flowOn(Dispatchers.IO)
    }

//    override fun fetchTransactionsWithCategoriesFromPeriod(
//        first: LocalDate,
//        second: LocalDate
//    ): Flow<List<TransactionWithCategory>?> {
//        return dataSource.fetchTransactionsWithCategoriesFromPeriod(first, second).flowOn(Dispatchers.IO)
//    }
}