package com.example.finance5

import android.app.Application
import com.example.finance5.data.repository.TransactionRepository

class FinanceApplication : Application() {

    // Создаем единственный экземпляр вашего обычного класса репозитория
    // lazy гарантирует, что он создастся только при первом обращении
    val transactionRepository: TransactionRepository by lazy {
        TransactionRepository()
    }
}