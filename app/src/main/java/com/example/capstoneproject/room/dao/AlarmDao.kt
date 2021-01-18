package com.example.capstoneproject.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.capstoneproject.room.model.Alarm

@Dao
interface AlarmDao {

    // this method  inserts alarm items into room database
    @Insert
    suspend fun insert(alarm: Alarm)

    // this method  update an inserted alarm items into room database
    @Update
    suspend fun update(alarm:Alarm)

    // this method  deletes alarm items into room database
    @Delete
    suspend fun delete(alarm: Alarm)

    // this method  deletes all alarm items from room database
    @Query("DELETE FROM alarms_items")
    suspend fun deleteAllAlarms()

    // this method gets all alarm items from room database
    @Query("SELECT * FROM alarms_items")
    fun getAllAlarms():LiveData<List<Alarm>>
}