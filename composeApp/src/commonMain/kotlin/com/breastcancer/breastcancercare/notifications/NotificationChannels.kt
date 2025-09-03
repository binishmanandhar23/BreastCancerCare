package com.breastcancer.breastcancercare.notifications

sealed class NotificationChannels(val channelId: String, val channelName: String) {
    object EventNotificationChannel : NotificationChannels("event_notification_channel", "Event Notifications")
    object ProgramNotificationChannel : NotificationChannels("program_notification_channel", "Program Notifications")
}