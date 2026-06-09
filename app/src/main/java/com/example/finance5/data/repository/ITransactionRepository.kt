package com.example.finance5.data.repository

import com.example.finance5.data.entity.Transaction
import com.example.finance5.data.entity.TransactionWithCategory
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ITransactionRepository {
    fun fetchTransactions() : Flow<List<Transaction>>

    suspend fun insertTransaction(transaction: Transaction)

    suspend fun updateTransaction(transaction: Transaction)

    suspend fun deleteTransaction(transaction: Transaction)

    fun fetchTransactionsWithCategories() : Flow<List<TransactionWithCategory>>

//    fun fetchTransactionsWithCategoriesFromPeriod(
//        first: LocalDate,
//        second: LocalDate
//    ): Flow<List<TransactionWithCategory>?>
}