package com.example.rumedcalendar.data.local.dao

import androidx.room.*
import com.example.rumedcalendar.data.local.entity.DocumentEntity
import com.example.rumedcalendar.data.local.entity.EventEntity
import com.example.rumedcalendar.data.local.entity.MedicationEntity
import com.example.rumedcalendar.data.local.entity.MedicationLogEntity
import com.example.rumedcalendar.domain.LogStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM events")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE start_time >= :start AND end_time <= :end")
    fun getEventsInRange(start: Long, end: Long): Flow<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity): Long

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Delete
    suspend fun deleteEvent(event: EventEntity)
}

@Dao
interface MedicationDao {
    @Query("SELECT * FROM medications")
    fun getAllMedications(): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medications WHERE id = :id")
    suspend fun getMedicationById(id: Long): MedicationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medication: MedicationEntity): Long

    @Update
    suspend fun updateMedication(medication: MedicationEntity)

    @Delete
    suspend fun deleteMedication(medication: MedicationEntity)
}

@Dao
interface MedicationLogDao {
    @Query("SELECT * FROM medication_logs WHERE medication_id = :medicationId ORDER BY scheduled_time DESC")
    fun getLogsForMedication(medicationId: Long): Flow<List<MedicationLogEntity>>

    @Query("SELECT * FROM medication_logs WHERE scheduled_time >= :start AND scheduled_time <= :end")
    fun getLogsInRange(start: Long, end: Long): Flow<List<MedicationLogEntity>>

    @Query("SELECT * FROM medication_logs WHERE status = :status AND scheduled_time < :currentTime")
    suspend fun getPendingLogsPastTime(status: LogStatus, currentTime: Long): List<MedicationLogEntity>

    @Query("SELECT * FROM medication_logs WHERE medication_id = :medicationId ORDER BY scheduled_time ASC")
    suspend fun getAllLogsForMedicationSync(medicationId: Long): List<MedicationLogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: MedicationLogEntity): Long

    @Update
    suspend fun updateLog(log: MedicationLogEntity)
}

@Dao
interface DocumentDao {
    @Query("SELECT * FROM documents")
    fun getAllDocuments(): Flow<List<DocumentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: DocumentEntity): Long

    @Delete
    suspend fun deleteDocument(document: DocumentEntity)
}
