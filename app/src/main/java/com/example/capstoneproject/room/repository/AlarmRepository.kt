package com.example.capstoneproject.room.repository

import com.example.capstoneproject.room.database.AlarmDatabase
import com.example.capstoneproject.room.model.Alarm


class AlarmRepository(private val db: AlarmDatabase) {
    suspend fun insert(alarm: Alarm) = db.getAlarmDao().insert(alarm)
    suspend fun update(alarm: Alarm) = db.getAlarmDao().update(alarm)
    suspend fun delete(alarm: Alarm) = db.getAlarmDao().delete(alarm)
    fun getAllAlarms() = db.getAlarmDao().getAllAlarms()
}