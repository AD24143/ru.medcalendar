package com.example.rumedcalendar.ui.screens.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rumedcalendar.ui.theme.MedCalTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

enum class DayEventType {
    DOCTOR_VISIT,
    MEDICATION,
    LAB_TEST
}

data class DayEventUi(
    val id: String,
    val title: String,
    val timeText: String,
    val details: String,
    val type: DayEventType,
    val isTaken: Boolean = false
)

@Composable
fun CalendarDayScreen(
    date: LocalDate,
    events: List<DayEventUi>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onTakenClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh
    )

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .pullRefresh(pullRefreshState)
        ) {
            Column {
                Text(
                    text = "Day View",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                Text(
                    text = date.format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy", Locale.ENGLISH)),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (events.isEmpty()) {
                        item {
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "No events for this day.",
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    } else {
                        items(events, key = { it.id }) { event ->
                            DayEventCard(
                                event = event,
                                onTakenClick = { onTakenClick(event.id) }
                            )
                        }
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun DayEventCard(
    event: DayEventUi,
    onTakenClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${event.timeText}  |  ${eventTypeLabel(event.type)}",
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = event.details,
                style = MaterialTheme.typography.bodyMedium
            )
            if (event.type == DayEventType.MEDICATION) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    OutlinedButton(onClick = onTakenClick, enabled = !event.isTaken) {
                        Text(text = "Taken")
                    }
                }
            }
        }
    }
}

private fun eventTypeLabel(type: DayEventType): String {
    return when (type) {
        DayEventType.DOCTOR_VISIT -> "Doctor Visit"
        DayEventType.MEDICATION -> "Medication"
        DayEventType.LAB_TEST -> "Lab Test"
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarDayScreenPreview() {
    MedCalTheme {
        CalendarDayScreen(
            date = LocalDate.now(),
            events = listOf(
                DayEventUi(
                    id = "1",
                    title = "Metformin 500 mg",
                    timeText = "08:00",
                    details = "Take after breakfast.",
                    type = DayEventType.MEDICATION
                ),
                DayEventUi(
                    id = "2",
                    title = "Cardiologist Visit",
                    timeText = "14:30",
                    details = "Bring previous ECG results.",
                    type = DayEventType.DOCTOR_VISIT
                )
            ),
            isRefreshing = false,
            onRefresh = {},
            onTakenClick = {}
        )
    }
}
