package com.example.rumedcalendar.reminder

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReminderBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val logId = intent.getLongExtra(EXTRA_LOG_ID, -1L)
        if (logId < 0L) return
        val medicationName = intent.getStringExtra(EXTRA_MEDICATION_NAME) ?: "Medication"
        val action = intent.action

        when (action) {
            ACTION_TAKEN -> {
                NotificationManagerCompat.from(context).cancel(logId.toInt())
                return
            }

            ACTION_SNOOZE -> {
                scheduleSnoozeReminder(
                    context = context,
                    logId = logId,
                    medicationName = medicationName
                )
                NotificationManagerCompat.from(context).cancel(logId.toInt())
                return
            }
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Medication Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val takenIntent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            action = ACTION_TAKEN
            putExtra(EXTRA_LOG_ID, logId)
            putExtra(EXTRA_MEDICATION_NAME, medicationName)
        }
        val snoozeIntent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            action = ACTION_SNOOZE
            putExtra(EXTRA_LOG_ID, logId)
            putExtra(EXTRA_MEDICATION_NAME, medicationName)
        }
        val takenPendingIntent = PendingIntent.getBroadcast(
            context,
            (logId * 10L + 1L).toInt(),
            takenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context,
            (logId * 10L + 2L).toInt(),
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Medication Reminder")
            .setContentText("It's almost time to take $medicationName")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .addAction(0, "Taken", takenPendingIntent)
            .addAction(0, "Snooze", snoozePendingIntent)
            .build()

        notificationManager.notify(logId.toInt(), notification)
    }

    private fun scheduleSnoozeReminder(
        context: Context,
        logId: Long,
        medicationName: String
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val snoozeIntent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra(EXTRA_LOG_ID, logId)
            putExtra(EXTRA_MEDICATION_NAME, medicationName)
        }
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context,
            (logId * 10L + 3L).toInt(),
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val triggerAtMillis = System.currentTimeMillis() + SNOOZE_DURATION_MILLIS
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            snoozePendingIntent
        )
    }

    companion object {
        private const val CHANNEL_ID = "medication_reminders"
        private const val EXTRA_LOG_ID = "EXTRA_LOG_ID"
        private const val EXTRA_MEDICATION_NAME = "EXTRA_MEDICATION_NAME"
        private const val ACTION_TAKEN = "com.example.rumedcalendar.ACTION_TAKEN"
        private const val ACTION_SNOOZE = "com.example.rumedcalendar.ACTION_SNOOZE"
        private const val SNOOZE_DURATION_MILLIS = 10 * 60 * 1000L
    }
}
