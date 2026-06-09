package com.example.finance5

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.example.finance5.data.entity.Transaction
import com.example.finance5.data.repository.TransactionRepository
import com.example.finance5.ui.viewmodel.TransactionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import kotlin.time.ExperimentalTime
import java.time.Instant

class MyNotificationListener: NotificationListenerService() {
    private lateinit var repository: TransactionRepository

    private val serviceScope = CoroutineScope(Dispatchers.IO) // Создаем область видимости для корутин (работа в фоне на IO-потоках)

    // Системный метод жизненного цикла службы
    override fun onCreate() {
        super.onCreate()
        // Здесь applicationContext гарантированно существует и готов к работе
        repository = (applicationContext as FinanceApplication).transactionRepository
    }

    // Срабатывает при появлении нового уведомления в шторке
    @OptIn(ExperimentalTime::class)
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn?.let {
            val packageName = it.packageName

            // Быстрая фильтрация по пакету на Главном потоке (работает мгновенно)
            if (packageName != "ru.sberbankmobile") return

            val extras = it.notification.extras
            val title = extras.getString("android.title") ?: ""
            val text = extras.getCharSequence("android.text")?.toString() ?: ""
            val timestamp = it.postTime

            if (!title.contains("Покупка", ignoreCase = true)) return

            // ВСЮ тяжелую работу (парсинг строк, конвертацию даты и запись в БД)
            // уводим внутрь корутины на фоновый поток IO
            serviceScope.launch {
                try {
                    // Безопасный парсинг суммы
                    val amountString = text.substringAfter(" ").substringBefore(" ")
                    val amount = amountString.toDoubleOrNull() ?: 0.0

                    // Инициализация конвертера на фоновом потоке
                    //val dateConverter = DateConverter()
                    //val transactionDate = dateConverter.toLocalDate(timestamp.toString())
                    // ПРАВИЛЬНАЯ конвертация Long-таймстампа в LocalDate
                    val transactionDate: LocalDate = Instant.ofEpochMilli(timestamp)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()

                    val transaction = Transaction(
                        amount = amount,
                        categoryId = 26,
                        date = transactionDate
                    )

                    // Теперь вызов гарантированно безопасен
                    repository.insertTransaction(transaction)

                } catch (e: Exception) {
                    // Логируем ошибку, чтобы служба не падала при непредвиденных форматах текста
                    android.util.Log.e(
                        "NotificationLog",
                        "Ошибка парсинга или записи: ${e.message}"
                    )
                }
            }
        }
    }
}