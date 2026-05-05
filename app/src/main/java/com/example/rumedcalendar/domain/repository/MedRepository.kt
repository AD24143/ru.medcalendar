package com.example.rumedcalendar.domain.repository

import com.example.rumedcalendar.data.local.entity.DocumentEntity
import com.example.rumedcalendar.data.local.entity.EventEntity
import com.example.rumedcalendar.data.local.entity.MedicationEntity
import com.example.rumedcalendar.data.local.entity.MedicationLogEntity
import com.example.rumedcalendar.domain.LogStatus
import kotlinx.coroutines.flow.Flow

interface MedRepository {
    // Events
    fun getAllEvents(): Flow<List<EventEntity>>
    fun getEventsInRange(start: Long, end: Long): Flow<List<EventEntity>>
    suspend fun insertEvent(event: EventEntity): Long
    suspend fun updateEvent(event: EventEntity)
    suspend fun deleteEvent(event: EventEntity)

    // Medications
    fun getAllMedications(): Flow<List<MedicationEntity>>
    suspend fun getMedicationById(id: Long): MedicationEntity?
    suspend fun insertMedication(medication: MedicationEntity): Long
    suspend fun updateMedication(medication: MedicationEntity)
    suspend fun deleteMedication(medication: MedicationEntity)

    // Medication Logs
    fun getLogsForMedication(medicationId: Long): Flow<List<MedicationLogEntity>>
    fun getLogsInRange(start: Long, end: Long): Flow<List<MedicationLogEntity>>
    suspend fun getPendingLogsPastTime(status: LogStatus, currentTime: Long): List<MedicationLogEntity>
    suspend fun getAllLogsForMedicationSync(medicationId: Long): List<MedicationLogEntity>
    suspend fun insertLog(log: MedicationLogEntity): Long
    suspend fun updateLog(log: MedicationLogEntity)

    // Documents
    fun getAllDocuments(): Flow<List<DocumentEntity>>
    suspend fun insertDocument(document: DocumentEntity): Long
    suspend fun deleteDocument(document: DocumentEntity)
}
