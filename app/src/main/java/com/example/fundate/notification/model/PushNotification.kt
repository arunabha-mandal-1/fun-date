package com.example.fundate.notification.model

data class PushNotification(
    val notificationData: NotificationData,
    val to: String? = ""
)
