package com.example.capstoneproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.capstoneproject.R
import com.example.capstoneproject.notification.ACTION_STOP_ALARM
import com.example.capstoneproject.receiver.AlarmReceiver

class MainActivity : AppCompatActivity() {

    private var fm: androidx.fragment.app.FragmentManager?=null
    private var fragment1: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val action=intent!!.action

        //stop alarm sound on notification click
        if(action == ACTION_STOP_ALARM){
            AlarmReceiver.taskRingtone!!.stop()
            AlarmReceiver.vibrator!!.cancel()
        }

        fragment1 = AlarmFragment()
        fm = supportFragmentManager
        fm!!.beginTransaction().add(R.id.fragmentcontainer, fragment1!!, "1").commit()
    }

}

