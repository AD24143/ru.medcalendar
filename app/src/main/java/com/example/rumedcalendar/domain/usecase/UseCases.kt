package com.example.rumedcalendar.domain.usecase

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import com.example.rumedcalendar.domain.DocumentCategory
import com.example.rumedcalendar.domain.LogStatus
import com.example.rumedcalendar.domain.repository.MedRepository
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CalculateStreakUseCase @Inject constructor(
    private val repository: MedRepository
) {
    suspend operator fun invoke(medicationId: Long): Int {
        val logs = repository.getAllLogsForMedicationSync(medicationId)
        if (logs.isEmpty()) return 0

        var currentStreak = 0
        var streakBroken = false

        // Group logs by day
        val logsByDay = logs.groupBy {
            val cal = Calendar.getInstance()
            cal.timeInMillis = it.scheduledTime
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            cal.timeInMillis
        }

        val sortedDays = logsByDay.keys.sortedDescending()

        for (day in sortedDays) {
            val dailyLogs = logsByDay[day] ?: emptyList()
            // If all scheduled doses for the day were TAKEN, increment streak
            val allTaken = dailyLogs.all { it.status == LogStatus.TAKEN }
            
            if (allTaken) {
                currentStreak++
            } else {
                // Ignore future days or today if not yet missed
                val today = Calendar.getInstance()
                today.set(Calendar.HOUR_OF_DAY, 0)
                today.set(Calendar.MINUTE, 0)
                today.set(Calendar.SECOND, 0)
                today.set(Calendar.MILLISECOND, 0)
                
                if (day < today.timeInMillis) {
                    streakBroken = true
                    break
                }
            }
        }
        return currentStreak
    }
}

class CheckMissedDosesUseCase @Inject constructor(
    private val repository: MedRepository
) {
    suspend operator fun invoke() {
        val currentTime = System.currentTimeMillis()
        val twoHoursInMillis = TimeUnit.HOURS.toMillis(2)
        val thresholdTime = currentTime - twoHoursInMillis

        val pendingLogs = repository.getPendingLogsPastTime(LogStatus.PENDING, thresholdTime)
        for (log in pendingLogs) {
            repository.updateLog(log.copy(status = LogStatus.MISSED))
        }
    }
}

class DocumentAnalysisUseCase @Inject constructor() {
    suspend operator fun invoke(context: Context, imageUri: Uri): String {
        val image = InputImage.fromFilePath(context, imageUri)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        
        return try {
            val result = recognizer.process(image).await()
            result.text
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}

class ExportPdfUseCase @Inject constructor(
    private val repository: MedRepository
) {
    suspend operator fun invoke(context: Context, start: Long, end: Long): File? {
        val eventsFlow = repository.getEventsInRange(start, end)
        // Note: For simplicity in this non-UI logic, we are collecting the first emission.
        // In a real scenario, you might collect or use a suspend function that returns List directly.
        var events = emptyList<com.example.rumedcalendar.data.local.entity.EventEntity>()
        eventsFlow.collect { 
            events = it
            return@collect
        }

        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
        val page = document.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()
        paint.textSize = 14f

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        var yPosition = 50f

        canvas.drawText("Medical Calendar Report", 50f, yPosition, paint)
        yPosition += 40f

        for (event in events) {
            if (yPosition > 800f) {
                // Break and create new page logic if needed
                break 
            }
            val text = "${dateFormat.format(Date(event.startTime))} - ${event.title} (${event.status})"
            canvas.drawText(text, 50f, yPosition, paint)
            yPosition += 30f
        }

        document.finishPage(page)

        val dir = context.getExternalFilesDir(null)
        val file = File(dir, "medical_report_${System.currentTimeMillis()}.pdf")
        
        return try {
            val fos = FileOutputStream(file)
            document.writeTo(fos)
            document.close()
            fos.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
