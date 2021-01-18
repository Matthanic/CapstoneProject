package com.example.capstoneproject.service

import android.app.AlarmManager
import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.capstoneproject.notification.ACTION_SNOOZE_ALARM
import com.example.capstoneproject.notification.ACTION_STOP_ALARM
import com.example.capstoneproject.receiver.AlarmReceiver
import java.util.*

@Suppress("DEPRECATION")
class AlarmService : IntentService(AlarmService::class.java.simpleName) {
   private val notificationId = System.currentTimeMillis().toInt()
    override fun onHandleIntent(intent: Intent?) {
       val action=intent!!.action

        //stop alarm sound
        if(action == ACTION_STOP_ALARM){
            if(AlarmReceiver.taskRingtone!!.isPlaying){
                AlarmReceiver.taskRingtone!!.stop()
                AlarmReceiver.vibrator!!.cancel()
            }
        }
        //snooze
        else if(action== ACTION_SNOOZE_ALARM){
            snoozeAlarm()
        }
    }

    private fun snoozeAlarm() {
        //cancel previous alarm tone
        if(AlarmReceiver.taskRingtone!!.isPlaying){
            AlarmReceiver.taskRingtone!!.stop()
            AlarmReceiver.vibrator!!.cancel()
        }

        //Snooze the alarm for 30 seconds.
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, notificationId, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,Calendar.getInstance().timeInMillis +30*1000,pendingIntent)
    }
}