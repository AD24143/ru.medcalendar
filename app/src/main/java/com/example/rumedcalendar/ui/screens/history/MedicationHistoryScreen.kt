package com.example.rumedcalendar.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rumedcalendar.ui.theme.AccentGreen
import com.example.rumedcalendar.ui.theme.MedCalTheme
import java.time.DayOfWeek

data class MedicationHistoryUi(
    val takenCountLast30Days: Int,
    val scheduledCountLast30Days: Int,
    val currentStreakDays: Int,
    val weeklyProgress: List<WeeklyBarUi>
)

data class WeeklyBarUi(
    val day: DayOfWeek,
    val takenCount: Int,
    val totalCount: Int
)

@Composable
fun MedicationHistoryScreen(
    history: MedicationHistoryUi,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Medication History",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }

            item {
                Last30DaysCard(
                    takenCount = history.takenCountLast30Days,
                    scheduledCount = history.scheduledCountLast30Days
                )
            }

            item {
                StreakCard(streakDays = history.currentStreakDays)
            }

            item {
                WeeklyChartCard(weeklyProgress = history.weeklyProgress)
            }
        }
    }
}

@Composable
private fun Last30DaysCard(
    takenCount: Int,
    scheduledCount: Int
) {
    val percent = if (scheduledCount == 0) 0 else (takenCount * 100 / scheduledCount)
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Last 30 Days",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Taken: $takenCount / $scheduledCount",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Adherence: $percent%",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun StreakCard(streakDays: Int) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Current Streak",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$streakDays days",
                style = MaterialTheme.typography.headlineMedium,
                color = AccentGreen,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun WeeklyChartCard(weeklyProgress: List<WeeklyBarUi>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Weekly Progress",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                weeklyProgress.forEach { item ->
                    WeeklyBar(item = item, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun WeeklyBar(
    item: WeeklyBarUi,
    modifier: Modifier = Modifier
) {
    val ratio = if (item.totalCount == 0) 0f else item.takenCount.toFloat() / item.totalCount.toFloat()
    val barHeight = (120 * ratio).dp

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "${item.takenCount}/${item.totalCount}",
            style = MaterialTheme.typography.labelSmall
        )
        Box(
            modifier = Modifier
                .width(22.dp)
                .height(120.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(barHeight)
                    .clip(RoundedCornerShape(10.dp))
                    .background(AccentGreen)
            )
        }
        Text(
            text = dayLabel(item.day),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

private fun dayLabel(day: DayOfWeek): String {
    return when (day) {
        DayOfWeek.MONDAY -> "Mon"
        DayOfWeek.TUESDAY -> "Tue"
        DayOfWeek.WEDNESDAY -> "Wed"
        DayOfWeek.THURSDAY -> "Thu"
        DayOfWeek.FRIDAY -> "Fri"
        DayOfWeek.SATURDAY -> "Sat"
        DayOfWeek.SUNDAY -> "Sun"
    }
}

@Preview(showBackground = true)
@Composable
private fun MedicationHistoryPreview() {
    MedCalTheme {
        MedicationHistoryScreen(
            history = MedicationHistoryUi(
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
        )
    }
}
