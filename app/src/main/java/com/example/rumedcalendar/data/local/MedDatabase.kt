package com.example.rumedcalendar.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.rumedcalendar.data.local.converter.Converters
import com.example.rumedcalendar.data.local.dao.DocumentDao
import com.example.rumedcalendar.data.local.dao.EventDao
import com.example.rumedcalendar.data.local.dao.MedicationDao
import com.example.rumedcalendar.data.local.dao.MedicationLogDao
import com.example.rumedcalendar.data.local.entity.DocumentEntity
import com.example.rumedcalendar.data.local.entity.EventEntity
import com.example.rumedcalendar.data.local.entity.MedicationEntity
import com.example.rumedcalendar.data.local.entity.MedicationLogEntity

@Database(
    entities = [
        EventEntity::class,
        MedicationEntity::class,
        MedicationLogEntity::class,
        DocumentEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MedDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun medicationDao(): MedicationDao
    abstract fun medicationLogDao(): MedicationLogDao
    abstract fun documentDao(): DocumentDao
}
