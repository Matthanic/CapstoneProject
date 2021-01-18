package com.example.capstoneproject.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms_items")
class Alarm(
    @ColumnInfo(name = "alarm_time")
    var time:String,
    @ColumnInfo(name = "AlarmIsEnabled")
    var AlarmIsEnabled:Boolean=true
        )
{
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
}


