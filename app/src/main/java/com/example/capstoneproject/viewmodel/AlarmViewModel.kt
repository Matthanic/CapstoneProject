package com.example.capstoneproject.viewmodel

import androidx.lifecycle.ViewModel
import com.example.capstoneproject.room.model.Alarm
import com.example.capstoneproject.room.repository.AlarmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(private val repos: AlarmRepository) :ViewModel(){

    fun insert(alarm: Alarm)= CoroutineScope(Dispatchers.Main).launch {
        repos.insert(alarm)
    }
    fun update(alarm: Alarm)= CoroutineScope(Dispatchers.Main).launch {
        repos.update(alarm)
    }
    fun delete(alarm: Alarm)= CoroutineScope(Dispatchers.Main).launch {
        repos.delete(alarm)
    }

    fun getAllAlarms()=repos.getAllAlarms()
}