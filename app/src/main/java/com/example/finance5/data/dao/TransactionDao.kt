package com.example.finance5.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction as _Transaction
import androidx.room.Update
import com.example.finance5.data.entity.Transaction
import com.example.finance5.data.entity.TransactionWithCategory
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun fetchTransactions() : Flow<List<Transaction>>

    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @_Transaction
    @Query("SELECT * FROM transactions")
    fun fetchTransactionsWithCategories(): Flow<List<TransactionWithCategory>>

//    @_Transaction
//    @Query("SELECT * FROM transactions WHERE (date > :first & date < :second)")
//    fun fetchTransactionsWithCategoriesFromPeriod(first: LocalDate, second: LocalDate): Flow<List<TransactionWithCategory>>?
}