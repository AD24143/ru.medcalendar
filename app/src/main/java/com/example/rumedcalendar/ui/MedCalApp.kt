package com.example.rumedcalendar.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.rumedcalendar.ui.screens.history.MedicationHistoryScreen
import com.example.rumedcalendar.ui.screens.history.MedicationHistoryUi
import com.example.rumedcalendar.ui.screens.history.WeeklyBarUi
import java.time.DayOfWeek

@Composable
fun MedCalApp() {
    val history = remember {
        MedicationHistoryUi(
            takenCountLast30Days = 52,
            scheduledCountLast30Days = 60,
            currentStreakDays = 9,
            weeklyProgress = listOf(
                WeeklyBarUi(DayOfWeek.MONDAY, 2, 2),
                WeeklyBarUi(DayOfWeek.TUESDAY, 1, 2),
                WeeklyBarUi(DayOfWeek.WEDNESDAY, 2, 2),
                WeeklyBarUi(DayOfWeek.THURSDAY, 2, 3),
                WeeklyBarUi(DayOfWeek.FRIDAY, 1, 2),
                WeeklyBarUi(DayOfWeek.SATURDAY, 2, 2),
                WeeklyBarUi(DayOfWeek.SUNDAY, 0, 1)
            )
        )
    }

    MedicationHistoryScreen(history = history)
}
