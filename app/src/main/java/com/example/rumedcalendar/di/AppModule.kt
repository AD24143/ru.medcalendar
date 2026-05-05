package com.example.rumedcalendar.di

import android.content.Context
import androidx.room.Room
import com.example.rumedcalendar.data.local.MedDatabase
import com.example.rumedcalendar.data.local.dao.DocumentDao
import com.example.rumedcalendar.data.local.dao.EventDao
import com.example.rumedcalendar.data.local.dao.MedicationDao
import com.example.rumedcalendar.data.local.dao.MedicationLogDao
import com.example.rumedcalendar.data.repository.MedRepositoryImpl
import com.example.rumedcalendar.domain.repository.MedRepository
import com.example.rumedcalendar.reminder.ReminderManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMedDatabase(@ApplicationContext context: Context): MedDatabase {
        return Room.databaseBuilder(
            context,
            MedDatabase::class.java,
            "med_calendar_db"
        ).build()
    }

    @Provides
    fun provideEventDao(database: MedDatabase): EventDao = database.eventDao()

    @Provides
    fun provideMedicationDao(database: MedDatabase): MedicationDao = database.medicationDao()

    @Provides
    fun provideMedicationLogDao(database: MedDatabase): MedicationLogDao = database.medicationLogDao()

    @Provides
    fun provideDocumentDao(database: MedDatabase): DocumentDao = database.documentDao()

    @Provides
    @Singleton
    fun provideMedRepository(
        eventDao: EventDao,
        medicationDao: MedicationDao,
        medicationLogDao: MedicationLogDao,
        documentDao: DocumentDao
    ): MedRepository {
        return MedRepositoryImpl(eventDao, medicationDao, medicationLogDao, documentDao)
    }

    @Provides
    @Singleton
    fun provideReminderManager(@ApplicationContext context: Context): ReminderManager {
        return ReminderManager(context)
    }
}
