package com.example.rumedcalendar.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rumedcalendar.ui.theme.EventLabBlue
import com.example.rumedcalendar.ui.theme.EventMedicationGreen
import com.example.rumedcalendar.ui.theme.EventVisitOrange
import com.example.rumedcalendar.ui.theme.EventVisitRed
import com.example.rumedcalendar.ui.theme.EventVisitYellow
import com.example.rumedcalendar.ui.theme.MedCalTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

enum class CalendarEventType {
    DOCTOR_RED,
    DOCTOR_ORANGE,
    DOCTOR_YELLOW,
    MEDICATION,
    LAB
}

data class CalendarEventDotUi(
    val type: CalendarEventType
)

@Composable
fun CalendarMonthScreen(
    eventsByDate: Map<LocalDate, List<CalendarEventDotUi>>,
    modifier: Modifier = Modifier
) {
    val today = remember { LocalDate.now() }
    val totalPages = 1200
    val initialPage = totalPages / 2
    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { totalPages })

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp)
        ) { page ->
            val month = YearMonth.now().plusMonths((page - initialPage).toLong())
            MonthPage(
                month = month,
                selectedDate = today,
                eventsByDate = eventsByDate
            )
        }
    }
}

@Composable
private fun MonthPage(
    month: YearMonth,
    selectedDate: LocalDate,
    eventsByDate: Map<LocalDate, List<CalendarEventDotUi>>
) {
    val firstDay = month.atDay(1)
    val daysInMonth = month.lengthOfMonth()
    val leadingEmptyCells = dayOfWeekColumn(firstDay.dayOfWeek)
    val totalCells = ((leadingEmptyCells + daysInMonth + 6) / 7) * 7

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "${month.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)} ${month.year}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            weekDaysOrdered().forEach { day ->
                Text(
                    text = day.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        repeat(totalCells / 7) { rowIndex ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(7) { colIndex ->
                    val cellIndex = rowIndex * 7 + colIndex
                    val dayNumber = cellIndex - leadingEmptyCells + 1
                    val date = if (dayNumber in 1..daysInMonth) month.atDay(dayNumber) else null
                    DayCell(
                        date = date,
                        isToday = date == selectedDate,
                        dots = date?.let { eventsByDate[it].orEmpty() }.orEmpty(),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    date: LocalDate?,
    isToday: Boolean,
    dots: List<CalendarEventDotUi>,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isToday) MaterialTheme.colorScheme.primary else Color.Transparent
    Column(
        modifier = modifier
            .height(60.dp)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(6.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = date?.dayOfMonth?.toString().orEmpty(),
            style = MaterialTheme.typography.bodySmall,
            color = if (date == null) Color.Transparent else MaterialTheme.colorScheme.onSurface
        )
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            dots.take(3).forEach { dot ->
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(color = dotColor(dot.type), shape = CircleShape)
                )
            }
        }
    }
}

@Composable
private fun dotColor(type: CalendarEventType): Color {
    return when (type) {
        CalendarEventType.DOCTOR_RED -> EventVisitRed
        CalendarEventType.DOCTOR_ORANGE -> EventVisitOrange
        CalendarEventType.DOCTOR_YELLOW -> EventVisitYellow
        CalendarEventType.MEDICATION -> EventMedicationGreen
        CalendarEventType.LAB -> EventLabBlue
    }
}

private fun dayOfWeekColumn(dayOfWeek: DayOfWeek): Int {
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> 0
        DayOfWeek.TUESDAY -> 1
        DayOfWeek.WEDNESDAY -> 2
        DayOfWeek.THURSDAY -> 3
        DayOfWeek.FRIDAY -> 4
        DayOfWeek.SATURDAY -> 5
        DayOfWeek.SUNDAY -> 6
    }
}

private fun weekDaysOrdered(): List<DayOfWeek> = listOf(
    DayOfWeek.MONDAY,
    DayOfWeek.TUESDAY,
    DayOfWeek.WEDNESDAY,
    DayOfWeek.THURSDAY,
    DayOfWeek.FRIDAY,
    DayOfWeek.SATURDAY,
    DayOfWeek.SUNDAY
)

@Preview(showBackground = true)
@Composable
private fun CalendarMonthScreenPreview() {
    val now = LocalDate.now()
    val sample = mapOf(
        now to listOf(
            CalendarEventDotUi(CalendarEventType.MEDICATION),
            CalendarEventDotUi(CalendarEventType.LAB)
        ),
        now.plusDays(2) to listOf(
            CalendarEventDotUi(CalendarEventType.DOCTOR_RED),
            CalendarEventDotUi(CalendarEventType.DOCTOR_ORANGE),
            CalendarEventDotUi(CalendarEventType.DOCTOR_YELLOW)
        )
    )

    MedCalTheme {
        CalendarMonthScreen(eventsByDate = sample)
    }
}
