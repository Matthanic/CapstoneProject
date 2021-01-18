package com.example.capstoneproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstoneproject.R
import com.example.capstoneproject.room.model.Alarm
import com.example.capstoneproject.viewmodel.AlarmViewModel
import com.example.capstoneproject.ui.CreateAlarmActivity
import kotlinx.android.synthetic.main.alarm_item.view.*

class AlarmAdapter(
    private val alarmViewModel: AlarmViewModel,
    var alarmList: List<Alarm>
) :
    RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alarm_item, parent, false)
        return AlarmViewHolder(view)
    }

    override fun getItemCount(): Int {

        return alarmList.size
    }

    fun getAlarmAt(position: Int): Alarm {
        return alarmList[position]
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val currentAlarm = alarmList[position]
        holder.itemView.tvTime.text = currentAlarm.time

        // Check whether the alarm is enabled or not
        holder.itemView.swActive.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                currentAlarm.AlarmIsEnabled = true

                CreateAlarmActivity.startAlarm(currentAlarm.id, holder.itemView.context)
                alarmViewModel.update(currentAlarm)
            } else {
                currentAlarm.AlarmIsEnabled = false
                CreateAlarmActivity.cancelAlarm(currentAlarm.id,holder.itemView.context)
                alarmViewModel.update(currentAlarm)
            }
        }


        holder.itemView.swActive.isChecked = currentAlarm.AlarmIsEnabled
    }


    inner class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
