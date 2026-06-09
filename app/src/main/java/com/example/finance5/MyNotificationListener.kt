package com.example.finance5

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.example.finance5.data.entity.Transaction
import com.example.finance5.ui.viewmodel.TransactionViewModel

class MyNotificationListener: NotificationListenerService() {

    // Срабатывает при появлении нового уведомления в шторке
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn?.let {
            val packageName = it.packageName
            val extras = it.notification.extras
            val title = extras.getString("android.title") ?: ""
            val text = extras.getCharSequence("android.text")?.toString() ?: ""
            val timestamp = it.postTime // Получаем системное время получения уведомления (в миллисекундах)
            val dateConverter = DateConverter() // Конвертер даты

            if (packageName != "ru.sberbankmobile")
                return

            if (title.substringBefore(" ") != "Покупка")
                return

            val amount = text.substringAfter(" ").substringBefore(" ").toDouble()

            // Передаем данные в модель
            val transaction = Transaction(
                amount = amount,
                categoryId = 0,
                date = dateConverter.toLocalDate(timestamp.toString())
            )

//            val transactionInfo = NotificationData(
//                packageName = packageName,
//                title = title,
//                text = text,
//                timestamp = timestamp
//            )

            transactionViewModel.insertTransaction(transaction)

            //NotificationStorage.addNotification(transactionInfo)

//            val packageName = it.packageName // Имя пакета приложения (например, com.whatsapp)
//            val extras = it.notification.extras
//            val title = extras.getString("android.title") ?: ""
//            val text = extras.getCharSequence("android.text")?.toString() ?: ""
//
//            //Log.d("NotificationLog", "Приложение: $packageName | Заголовок: $title | Текст: $text")
//
//            // Сохраняем уведомление в синглтон/репозиторий
//            NotificationStorage.addNotification("[$packageName] $title: $text")
        }
    }
}