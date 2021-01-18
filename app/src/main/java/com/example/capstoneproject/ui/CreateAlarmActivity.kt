package com.example.capstoneproject.ui

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capstoneproject.R
import com.example.capstoneproject.receiver.AlarmReceiver
import kotlinx.android.synthetic.main.activity_create_alarm.*
import java.util.*


class CreateAlarmActivity : AppCompatActivity() {

    private lateinit var amOrPm: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_alarm)

        // Set title
        title = getString(R.string.create_alarm_title)

        initButtons()

    }
    private fun initButtons(){

        btnChooseTime.setOnClickListener {
            chooseTime()
        }

        btnSetAlarm.setOnClickListener {
            setAlarm()
        }

    }

    private fun chooseTime() {
        // Get a timepicker for selecting the alarm
        val calendar = Calendar.getInstance()
        TimePickerDialog(this, { _, hour, min ->
            selectedHour = hour
            selectedMin = min

            var hourString = selectedHour.toString()
            var minString = selectedMin.toString()
            if (selectedHour > 12) {
                hourString = (selectedHour - 12).toString()
                amOrPm = "PM"
            } else {
                amOrPm = "AM"
            }

            if (selectedMin < 10) {
                minString = "0$selectedMin"
            }
            val formattedTime = "$hourString:$minString $amOrPm"

            tvChosenTime.text = formattedTime
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
    }

    private fun setAlarm(){
        if(TextUtils.isEmpty(tvChosenTime.text)){
            Toast.makeText(this, "Please select a time!", Toast.LENGTH_SHORT).show()
            return
        }
        sendDataToAlarmFragment()
    }

    private fun sendDataToAlarmFragment() {
        val timeText = tvChosenTime.text.toString()
        val alarmIsOn = true

        val intent = Intent()
        intent.putExtra(ALARM_TIME, timeText)
        intent.putExtra(ALARM_IsON, alarmIsOn)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }


    companion object {
        private var selectedHour = 0
        private var selectedMin = 0

        fun startAlarm(alarmId:Int,context: Context) {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, selectedHour)
                set(Calendar.MINUTE, selectedMin)
            }
            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1)
            }

            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent =
                PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )

        }

        // Cancel alarm function
        fun cancelAlarm(id: Int, context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent =
                PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.cancel(pendingIntent)
        }

        const val ALARM_TIME = "ALARM_TIME"
        const val ALARM_IsON = "ALARM_IsON"
    }

}

