package com.example.rumedcalendar.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.rumedcalendar.ui.screens.calendar.CalendarDayScreen
import com.example.rumedcalendar.ui.screens.calendar.DayEventType
import com.example.rumedcalendar.ui.screens.calendar.DayEventUi
import java.time.LocalDate

@Composable
fun MedCalApp() {
    val today = remember { LocalDate.now() }
    var isRefreshing by remember { mutableStateOf(false) }
    var dayEvents by remember {
        mutableStateOf(
            listOf(
                DayEventUi(
                    id = "d1",
                    title = "Metformin 500 mg",
                    timeText = "08:00",
                    details = "Take after breakfast.",
                    type = DayEventType.MEDICATION
                ),
                DayEventUi(
                    id = "d2",
                    title = "Blood Test",
                    timeText = "11:30",
                    details = "Complete fasting panel.",
                    type = DayEventType.LAB_TEST
                ),
                DayEventUi(
                    id = "d3",
                    title = "Endocrinologist Visit",
                    timeText = "16:00",
                    details = "Discuss glucose trend.",
                    type = DayEventType.DOCTOR_VISIT
                )
            )
        )
    }

    CalendarDayScreen(
        date = today,
        events = dayEvents,
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            isRefreshing = false
        },
        onTakenClick = { id ->
            dayEvents = dayEvents.map { event ->
                if (event.id == id) event.copy(isTaken = true) else event
            }
        }
    )
}
