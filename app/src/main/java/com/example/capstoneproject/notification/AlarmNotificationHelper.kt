package com.example.capstoneproject.notification

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.capstoneproject.R
import com.example.capstoneproject.service.AlarmService
import com.example.capstoneproject.ui.MainActivity

const val ACTION_STOP_ALARM ="ACTION_STOP_ALARM"
const val ACTION_SNOOZE_ALARM ="ACTION_SNOOZE_ALARM"

class AlarmNotificationHelper(base:Context) :ContextWrapper(base) {

    private val notificationId = System.currentTimeMillis().toInt()
    private val MYCHANNEL_ID= "Alarm Notification ID"
    private val MYCHANNEL_NAME= "Alarm Alarm"

    private var manager:NotificationManager?=null

    // Check if the device version is greater or equals version O
    init {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            createChannels()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)

    // Create notification channel
    private fun createChannels(){
        val channel=NotificationChannel(
            MYCHANNEL_ID,
            MYCHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH)
        channel.enableVibration(true)
        getManager().createNotificationChannel(channel)
    }

    fun getManager():NotificationManager{
        if(manager==null) manager=
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return manager as NotificationManager
    }

    // Build notification functionality
    fun getNotificationBuilder():NotificationCompat.Builder{
        val intent=Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            action = ACTION_STOP_ALARM
        }
        val pendingIntent=PendingIntent.getActivity(this,
            notificationId,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        return NotificationCompat.Builder(applicationContext,MYCHANNEL_ID)
            .setContentText("Alarm")
            .setSmallIcon(R.drawable.ic_alarm)
            .setColor(Color.YELLOW)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOngoing (true)
            .addAction(R.drawable.ic_alarm_stop,"Stop",stopAlarmTone(this))
            .addAction(R.drawable.ic_snooze,"Snooze",snoozeAlarm(this))



    }
    // Stop alarm tone functionality
    private fun stopAlarmTone(context:Context):PendingIntent{
        val stopAlarmIntent=Intent(context, AlarmService::class.java).apply {
            action= ACTION_STOP_ALARM
        }
        return PendingIntent.getService(this, notificationId,stopAlarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)

    }

    // Snooze alarm
    private fun snoozeAlarm(context:Context):PendingIntent{
       val snoozeIntent=Intent(context,
           AlarmService::class.java).apply {
           action= ACTION_SNOOZE_ALARM
       }
        return PendingIntent.getService(this, notificationId,snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)
    }

}