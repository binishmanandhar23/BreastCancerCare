package com.breastcancer.breastcancercare.notifications

import android.app.NotificationManager
import com.breastcancer.breastcancercare.R
import com.tweener.alarmee.channel.AlarmeeNotificationChannel
import com.tweener.alarmee.configuration.AlarmeeAndroidPlatformConfiguration
import com.tweener.alarmee.configuration.AlarmeePlatformConfiguration

actual fun createAlarmeePlatformConfiguration(): AlarmeePlatformConfiguration =
    AlarmeeAndroidPlatformConfiguration(
        notificationIconResId = R.mipmap.ic_launcher,
        notificationIconColor = androidx.compose.ui.graphics.Color.Transparent, // Defaults to Color.Transparent is not specified
        notificationChannels = listOf(
            AlarmeeNotificationChannel(
                id = NotificationChannels.ActivityNotificationChannel.channelId,
                name = NotificationChannels.ActivityNotificationChannel.channelName,
                importance = NotificationManager.IMPORTANCE_HIGH,
            ),
            AlarmeeNotificationChannel(
                id = NotificationChannels.ProgramNotificationChannel.channelId,
                name = NotificationChannels.ProgramNotificationChannel.channelName,
                importance = NotificationManager.IMPORTANCE_LOW,
            ),
        )
    )