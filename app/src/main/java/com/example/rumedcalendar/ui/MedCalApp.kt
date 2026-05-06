package com.example.rumedcalendar.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.rumedcalendar.ui.screens.calendar.CalendarEventDotUi
import com.example.rumedcalendar.ui.screens.calendar.CalendarEventType
import com.example.rumedcalendar.ui.screens.calendar.CalendarMonthScreen
import java.time.LocalDate

@Composable
fun MedCalApp() {
    val today = remember { LocalDate.now() }
    val mockEvents = remember {
        mapOf(
            today to listOf(
                CalendarEventDotUi(type = CalendarEventType.MEDICATION),
                CalendarEventDotUi(type = CalendarEventType.LAB)
            ),
            today.plusDays(1) to listOf(CalendarEventDotUi(type = CalendarEventType.DOCTOR_ORANGE)),
            today.plusDays(3) to listOf(
                CalendarEventDotUi(type = CalendarEventType.DOCTOR_RED),
                CalendarEventDotUi(type = CalendarEventType.DOCTOR_YELLOW),
                CalendarEventDotUi(type = CalendarEventType.MEDICATION)
            )
        )
    }

    CalendarMonthScreen(eventsByDate = mockEvents)
}
