package com.example.rumedcalendar.ui

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.rumedcalendar.ui.screens.calendar.CalendarDayScreen
import com.example.rumedcalendar.ui.screens.calendar.CalendarEventDotUi
import com.example.rumedcalendar.ui.screens.calendar.CalendarEventType
import com.example.rumedcalendar.ui.screens.calendar.CalendarMonthScreen
import com.example.rumedcalendar.ui.screens.calendar.DayEventType
import com.example.rumedcalendar.ui.screens.calendar.DayEventUi
import com.example.rumedcalendar.ui.screens.history.MedicationHistoryScreen
import com.example.rumedcalendar.ui.screens.history.MedicationHistoryUi
import com.example.rumedcalendar.ui.screens.history.WeeklyBarUi
import com.example.rumedcalendar.ui.screens.upload.ExtractedUploadItemUi
import com.example.rumedcalendar.ui.screens.upload.UploadScreen
import java.time.DayOfWeek
import java.time.LocalDate

private enum class MainScreenTab(val title: String) {
    UPLOAD("Upload"),
    MONTH("Month"),
    DAY("Day"),
    HISTORY("History")
}

@Composable
fun MedCalApp() {
    var selectedTab by remember { mutableStateOf(MainScreenTab.UPLOAD) }
    val today = remember { LocalDate.now() }

    var extractedItems by remember {
        mutableStateOf(
            listOf(
                ExtractedUploadItemUi(
                    id = "u1",
                    title = "Amoxicillin 500 mg",
                    details = "Take 1 tablet twice daily for 7 days."
                ),
                ExtractedUploadItemUi(
                    id = "u2",
                    title = "Dermatology Follow-up",
                    details = "Visit clinic next Thursday at 09:30 AM."
                )
            )
        )
    }

    val monthEvents = remember {
        mapOf(
            today to listOf(
                CalendarEventDotUi(CalendarEventType.MEDICATION),
                CalendarEventDotUi(CalendarEventType.LAB)
            ),
            today.plusDays(1) to listOf(CalendarEventDotUi(CalendarEventType.DOCTOR_ORANGE)),
            today.plusDays(4) to listOf(
                CalendarEventDotUi(CalendarEventType.DOCTOR_RED),
                CalendarEventDotUi(CalendarEventType.DOCTOR_YELLOW)
            )
        )
    }

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

    Scaffold(
        bottomBar = {
            NavigationBar {
                MainScreenTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = { Text(tab.title.take(1)) },
                        label = { Text(tab.title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            MainScreenTab.UPLOAD -> UploadScreen(
                extractedItems = extractedItems,
                onPickFileClick = {},
                onTakePhotoClick = {},
                onConfirmItemClick = { id ->
                    extractedItems = extractedItems.filterNot { it.id == id }
                },
                modifier = androidx.compose.ui.Modifier.padding(innerPadding)
            )

            MainScreenTab.MONTH -> CalendarMonthScreen(
                eventsByDate = monthEvents,
                modifier = androidx.compose.ui.Modifier.padding(innerPadding)
            )

            MainScreenTab.DAY -> CalendarDayScreen(
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
                },
                modifier = androidx.compose.ui.Modifier.padding(innerPadding)
            )

            MainScreenTab.HISTORY -> MedicationHistoryScreen(
                history = history,
                modifier = androidx.compose.ui.Modifier.padding(innerPadding)
            )
        }
    }
}
