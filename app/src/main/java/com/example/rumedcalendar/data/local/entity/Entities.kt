package com.example.rumedcalendar.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rumedcalendar.domain.DocumentCategory
import com.example.rumedcalendar.domain.EventStatus
import com.example.rumedcalendar.domain.EventType
import com.example.rumedcalendar.domain.LogStatus
import com.example.rumedcalendar.domain.Priority
import com.example.rumedcalendar.domain.ScheduleType

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String?,
    @ColumnInfo(name = "event_type") val eventType: EventType,
    val priority: Priority,
    @ColumnInfo(name = "start_time") val startTime: Long,
    @ColumnInfo(name = "end_time") val endTime: Long,
    @ColumnInfo(name = "is_all_day") val isAllDay: Boolean,
    val status: EventStatus,
    @ColumnInfo(name = "medication_id") val medicationId: Long?,
    val color: Int?,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "medications")
data class MedicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "event_id") val eventId: Long?,
    val name: String,
    val dosage: String,
    val unit: String,
    @ColumnInfo(name = "schedule_type") val scheduleType: ScheduleType,
    @ColumnInfo(name = "schedule_rule") val scheduleRule: String?, // JSON string
    @ColumnInfo(name = "start_date") val startDate: Long,
    @ColumnInfo(name = "end_date") val endDate: Long?,
    @ColumnInfo(name = "total_pills") val totalPills: Int?,
    @ColumnInfo(name = "remaining_pills") val remainingPills: Int?,
    val instructions: String?,
    val color: Int?
)

@Entity(tableName = "medication_logs")
data class MedicationLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "medication_id") val medicationId: Long,
    @ColumnInfo(name = "event_id") val eventId: Long?,
    @ColumnInfo(name = "scheduled_time") val scheduledTime: Long,
    @ColumnInfo(name = "taken_time") val takenTime: Long?,
    val status: LogStatus,
    val notes: String?
)

@Entity(tableName = "documents")
data class DocumentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "file_path") val filePath: String,
    @ColumnInfo(name = "ocr_text") val ocrText: String?,
    val category: DocumentCategory,
    @ColumnInfo(name = "extracted_event_id") val extractedEventId: Long?,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)
