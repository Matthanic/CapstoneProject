package com.example.capstoneproject.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.capstoneproject.room.dao.AlarmDao
import com.example.capstoneproject.room.model.Alarm


@Database(entities = [Alarm::class], version = 2, exportSchema = false)
abstract class AlarmDatabase : RoomDatabase() {

    //this method would be called in our alarm repos
    abstract fun getAlarmDao(): AlarmDao


    companion object {
        private var instance: AlarmDatabase? = null
        private val lock = Any()

        //this method is called every time this class is invoked
        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: createDatabase(context).also { instance = it }
        }


        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context,
            AlarmDatabase::class.java, "alarmDb.db"
        ).fallbackToDestructiveMigration().build()
    }
}

