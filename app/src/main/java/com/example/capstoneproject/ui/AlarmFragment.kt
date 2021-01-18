package com.example.capstoneproject.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstoneproject.R
import com.example.capstoneproject.adapter.AlarmAdapter
import com.example.capstoneproject.room.model.Alarm
import com.example.capstoneproject.viewmodel.AlarmViewModel
import com.example.capstoneproject.viewmodel.AlarmViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


@Suppress("RedundantSamConstructor")
class AlarmFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: AlarmViewModelFactory by instance()
    private var viewModel: AlarmViewModel? = null
    private var adapter: AlarmAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_alarm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intent = Intent(context, CreateAlarmActivity::class.java)
        val fab = view.findViewById<FloatingActionButton>(R.id.fabAddAlarm)

        //Fab for adding alarms.
        fab.setOnClickListener {
            startActivityForResult(intent, CREATE_ALARM_REQUEST)
        }

        initViews(view)

    }

    private fun initViews(view: View) {
        //Initialize the recycler view with a linear layout manager
        viewModel = ViewModelProvider(this, factory).get(AlarmViewModel::class.java)

        adapter = AlarmAdapter(viewModel!!, listOf())

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvAlarm)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        viewModel!!.getAllAlarms().observe(viewLifecycleOwner, Observer {
            adapter!!.alarmList = it
            adapter!!.notifyDataSetChanged()
        })

        createItemTouchHelper(view).attachToRecyclerView(recyclerView)

    }


    private fun createItemTouchHelper(view: View): ItemTouchHelper {
        // Swipe to the left to delete alarms
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.LEFT) {
                    val adapterPosition = viewHolder.adapterPosition
                    val deletedAlarm = adapter!!.getAlarmAt(adapterPosition)

                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.IO) {
                            viewModel!!.delete(deletedAlarm)
                            Snackbar.make(view, getString(R.string.alarm_deleted), Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        return ItemTouchHelper(callback)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_CANCELED && data != null) {
            // Get intent data from create alarm activity
            if (requestCode == CREATE_ALARM_REQUEST && resultCode == Activity.RESULT_OK) {
                val time = data.getStringExtra(CreateAlarmActivity.ALARM_TIME)
                val alarmIsActive = data.getBooleanExtra(CreateAlarmActivity.ALARM_IsON, true)

                val alarm = Alarm(time!!, alarmIsActive)
                // Insert the alarm into database using our viewModel instance
                viewModel!!.insert(alarm)
            }
        }

    }

    companion object {
        const val CREATE_ALARM_REQUEST = 1
    }
}





