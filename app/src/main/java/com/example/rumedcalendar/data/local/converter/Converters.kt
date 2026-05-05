package com.example.rumedcalendar.data.local.converter

import androidx.room.TypeConverter
import com.example.rumedcalendar.domain.DocumentCategory
import com.example.rumedcalendar.domain.EventStatus
import com.example.rumedcalendar.domain.EventType
import com.example.rumedcalendar.domain.LogStatus
import com.example.rumedcalendar.domain.Priority
import com.example.rumedcalendar.domain.ScheduleType

class Converters {
    @TypeConverter
    fun fromEventType(value: EventType) = value.name
    @TypeConverter
    fun toEventType(value: String) = enumValueOf<EventType>(value)

    @TypeConverter
    fun fromPriority(value: Priority) = value.name
    @TypeConverter
    fun toPriority(value: String) = enumValueOf<Priority>(value)

    @TypeConverter
    fun fromEventStatus(value: EventStatus) = value.name
    @TypeConverter
    fun toEventStatus(value: String) = enumValueOf<EventStatus>(value)

    @TypeConverter
    fun fromScheduleType(value: ScheduleType) = value.name
    @TypeConverter
    fun toScheduleType(value: String) = enumValueOf<ScheduleType>(value)

    @TypeConverter
    fun fromLogStatus(value: LogStatus) = value.name
    @TypeConverter
    fun toLogStatus(value: String) = enumValueOf<LogStatus>(value)

    @TypeConverter
    fun fromDocumentCategory(value: DocumentCategory) = value.name
    @TypeConverter
    fun toDocumentCategory(value: String) = enumValueOf<DocumentCategory>(value)
}
