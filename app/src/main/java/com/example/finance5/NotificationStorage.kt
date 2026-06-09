package com.example.finance5

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

//object NotificationStorage {
//    private val _notifications = MutableStateFlow<List<NotificationData>>(emptyList())
//    val notifications: StateFlow<List<NotificationData>> = _notifications.asStateFlow()
//
//    fun addNotification(data: NotificationData) {
//        _notifications.value = _notifications.value + data
//    }
//}