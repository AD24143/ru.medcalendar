package com.example.rumedcalendar.domain

enum class EventType { MEDICATION, DOCTOR_VISIT, LAB_TEST, REMINDER }

enum class Priority { HIGH, MEDIUM, LOW }

enum class EventStatus { SCHEDULED, COMPLETED, MISSED, CANCELLED }

enum class ScheduleType { DAILY, WEEKLY, AS_NEEDED, CUSTOM }

enum class LogStatus { PENDING, TAKEN, MISSED }

enum class DocumentCategory { PRESCRIPTION, LAB_RESULT, APPOINTMENT_CARD, OTHER }
