package com.example.finance5

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import com.example.finance5.data.entity.Transaction

//class SmsReceiver : BroadcastReceiver() {
//
//    override fun onReceive(context: Context?, intent: Intent?) {
//        if (intent?.action == "android.provider.Telephony.SMS_RECEIVED") {
//            val bundle = intent.extras
//            if (bundle != null) {
//                try {
//                    // Извлекаем массив PDU (Protocol Data Unit)
//                    val pdus = bundle.get("pdus") as? Array<*>
//                    if (pdus != null) {
//                        val format = bundle.getString("format")
//
//                        var sender = ""
//                        var fullMessageText = ""
//                        var timestamp = System.currentTimeMillis()
//
//                        for (pdu in pdus) {
//                            val smsMessage = SmsMessage.createFromPdu(pdu as ByteArray, format)
//                            sender = smsMessage.displayOriginatingAddress ?: "Неизвестный"
//                            fullMessageText += smsMessage.displayMessageBody ?: ""
//                            timestamp = smsMessage.timestampMillis // Время отправки сетью
//                        }
//
//                        Log.d("SmsLog", "От: $sender | Текст: $fullMessageText")
//
//                        // Приводим к общему data-классу и сохраняем в наше единое хранилище
//                        val transaction = Transaction(
//                            amount = ,
//                            categoryId = ,
//                            date = timestamp
//                        )
//                        val unifiedData = NotificationData(
//                            packageName = "SMS: $sender", // Помечаем, что это SMS
//                            title = "Входящее сообщение",
//                            text = fullMessageText,
//                            timestamp =
//                        )
//
//                        NotificationStorage.addNotification(unifiedData)
//                    }
//                } catch (e: Exception) {
//                    Log.e("SmsLog", "Ошибка обработки SMS", e)
//                }
//            }
//        }
//    }
//}