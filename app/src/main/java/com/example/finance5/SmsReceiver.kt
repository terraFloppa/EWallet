package com.example.finance5

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import com.example.finance5.data.entity.Transaction
import com.example.finance5.data.repository.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class SmsReceiver : BroadcastReceiver() {

    // Репозиторий больше не lateinit, мы получим его безопасно в onReceive
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    override fun onReceive(context: Context?, intent: Intent?) {
        // 1. Проверяем экшен интента
        if (intent?.action != "android.provider.Telephony.SMS_RECEIVED") return

        // 2. Безопасно получаем контекст приложения и репозиторий
        val appContext = context?.applicationContext as? FinanceApplication ?: return
        val repository = appContext.transactionRepository

        val bundle = intent.extras
        // ИСПРАВЛЕНО: Прерываем работу, если бандл пустой (== null)
        if (bundle == null) return

        try {
            val pdus = bundle.get("pdus") as? Array<*>
            if (pdus == null) return

            val format = bundle.getString("format")
            var sender = ""
            var text = ""
            var timestamp = System.currentTimeMillis()

            for (pdu in pdus) {
                val smsMessage = SmsMessage.createFromPdu(pdu as ByteArray, format)
                sender = smsMessage.displayOriginatingAddress ?: "Неизвестный"
                text += smsMessage.displayMessageBody ?: ""
                timestamp = smsMessage.timestampMillis
            }

            Log.d("SmsLog", "От: $sender | Текст: $text")

            // Фильтруем отправителя (например, только от номера 900 или банка)
            if (sender != "900") return

            serviceScope.launch {
                try {
                    // ИСПРАВЛЕНО: Безопасный парсинг, защищенный от вылетов по индексам
                    val words = text.split(" ")
                    val amount = if (words.size > 5) {
                        // Чистим строку от лишних символов (точек, запятых, валюты) перед конвертацией
                        val cleanAmount = words[5].replace(Regex("[^0-9.]"), "")
                        cleanAmount.toDoubleOrNull() ?: 0.0
                    } else {
                        0.0
                    }

                    val transactionDate: LocalDate = Instant.ofEpochMilli(timestamp)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()

                    val transaction = Transaction(
                        amount = amount,
                        categoryId = 26, // Убедитесь, что категория с ID 26 создана в вашей БД
                        date = transactionDate
                    )

                    repository.insertTransaction(transaction)
                    Log.d("SmsLog", "СМС транзакция на сумму $amount успешно сохранена в Room")

                } catch (e: Exception) {
                    Log.e("SmsLog", "Ошибка парсинга текста СМС или записи: ${e.message}", e)
                }
            }
        } catch (e: Exception) {
            Log.e("SmsLog", "Ошибка извлечения данных СМС", e)
        }
    }
}
