package com.example.rumedcalendar.data.repository

import com.example.rumedcalendar.data.local.dao.DocumentDao
import com.example.rumedcalendar.data.local.dao.EventDao
import com.example.rumedcalendar.data.local.dao.MedicationDao
import com.example.rumedcalendar.data.local.dao.MedicationLogDao
import com.example.rumedcalendar.data.local.entity.DocumentEntity
import com.example.rumedcalendar.data.local.entity.EventEntity
import com.example.rumedcalendar.data.local.entity.MedicationEntity
import com.example.rumedcalendar.data.local.entity.MedicationLogEntity
import com.example.rumedcalendar.domain.LogStatus
import com.example.rumedcalendar.domain.repository.MedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MedRepositoryImpl @Inject constructor(
    private val eventDao: EventDao,
    private val medicationDao: MedicationDao,
    private val medicationLogDao: MedicationLogDao,
    private val documentDao: DocumentDao
) : MedRepository {

    override fun getAllEvents(): Flow<List<EventEntity>> = eventDao.getAllEvents()
    override fun getEventsInRange(start: Long, end: Long): Flow<List<EventEntity>> = eventDao.getEventsInRange(start, end)
    override suspend fun insertEvent(event: EventEntity): Long = eventDao.insertEvent(event)
    override suspend fun updateEvent(event: EventEntity) = eventDao.updateEvent(event)
    override suspend fun deleteEvent(event: EventEntity) = eventDao.deleteEvent(event)

    override fun getAllMedications(): Flow<List<MedicationEntity>> = medicationDao.getAllMedications()
    override suspend fun getMedicationById(id: Long): MedicationEntity? = medicationDao.getMedicationById(id)
    override suspend fun insertMedication(medication: MedicationEntity): Long = medicationDao.insertMedication(medication)
    override suspend fun updateMedication(medication: MedicationEntity) = medicationDao.updateMedication(medication)
    override suspend fun deleteMedication(medication: MedicationEntity) = medicationDao.deleteMedication(medication)

    override fun getLogsForMedication(medicationId: Long): Flow<List<MedicationLogEntity>> = medicationLogDao.getLogsForMedication(medicationId)
    override fun getLogsInRange(start: Long, end: Long): Flow<List<MedicationLogEntity>> = medicationLogDao.getLogsInRange(start, end)
    override suspend fun getPendingLogsPastTime(status: LogStatus, currentTime: Long): List<MedicationLogEntity> = medicationLogDao.getPendingLogsPastTime(status, currentTime)
    override suspend fun getAllLogsForMedicationSync(medicationId: Long): List<MedicationLogEntity> = medicationLogDao.getAllLogsForMedicationSync(medicationId)
    override suspend fun insertLog(log: MedicationLogEntity): Long = medicationLogDao.insertLog(log)
    override suspend fun updateLog(log: MedicationLogEntity) = medicationLogDao.updateLog(log)

    override fun getAllDocuments(): Flow<List<DocumentEntity>> = documentDao.getAllDocuments()
    override suspend fun insertDocument(document: DocumentEntity): Long = documentDao.insertDocument(document)
    override suspend fun deleteDocument(document: DocumentEntity) = documentDao.deleteDocument(document)
}
