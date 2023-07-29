package com.example.waterreminder.ui.composite_screen.settings.schedule

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.ColumnInfo
import com.example.waterreminder.base.BaseActivity
import com.example.waterreminder.database.reminder.ReminderEntity
import com.example.waterreminder.databinding.ActivityScheduleSettingBinding
import com.example.waterreminder.helper.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleSettingActivity : BaseActivity<ActivityScheduleSettingBinding>() {
    private val viewModel:ScheduleSettingViewModel by viewModels()


    @Inject
    lateinit var preferenceHelper: PreferenceHelper
    @Inject
    lateinit var adapter: ScheduleSettingAdapter


    private var isRemindersGenerated:Boolean? = null

    private val reminderList: MutableList<ReminderEntity> = mutableListOf()
    private val calendar = Calendar.getInstance()

    private lateinit var wakeUpTimeHour: String
    private lateinit var wakeUpTimeMinute: String
    private lateinit var sleepTimeHour: String
    private lateinit var sleepTimeMinute: String

    override fun initView() {
        initUi()
        observe()
        setListener()
    }


    override fun setBinding(layoutInflater: LayoutInflater): ActivityScheduleSettingBinding {
        return ActivityScheduleSettingBinding.inflate(layoutInflater)
    }

    private fun observe() {
        val layoutManager = LinearLayoutManager(this)
        binding.rcv.layoutManager = layoutManager
        binding.rcv.adapter = adapter
        viewModel.getReminders()
        viewModel.listReminder.observe(this){
            adapter.setData(it)
            adapter.notifyDataSetChanged()

           for (reminder in it){
               reminder.schedule(applicationContext)
           }
        }

        val intentService = Intent(applicationContext, AlarmService::class.java)
        applicationContext.stopService(intentService)

    }

    private fun initUi() {
        wakeUpTimeHour = preferenceHelper.getString(PreferenceHelper.PREF_WAKE_UP_TIME_HOUR).toString()
        wakeUpTimeMinute = preferenceHelper.getString(PreferenceHelper.PREF_WAKE_UP_TIME_MINUTE).toString()
        sleepTimeHour = preferenceHelper.getString(PreferenceHelper.PREF_SLEEP_TIME_HOUR).toString()
        sleepTimeMinute = preferenceHelper.getString(PreferenceHelper.PREF_SLEEP_TIME_MINUTE).toString()
        isRemindersGenerated = preferenceHelper.getBoolean(PreferenceHelper.PREF_IS_CHECK)
        if(isRemindersGenerated == true){
            generateReminders(wakeUpTimeHour.toInt(),wakeUpTimeMinute.toInt(),sleepTimeHour.toInt(),sleepTimeMinute.toInt())
            isRemindersGenerated = false
            preferenceHelper.setBoolean(PreferenceHelper.PREF_IS_CHECK, isRemindersGenerated!!)

        }



    }

    private fun setListener(){
        binding.imgBack.setOnClickListener {
            finish()
        }
        adapter.clickListener.onItemClick = { reminderEntity ->
            reminderEntity.isCheck = !reminderEntity.isCheck
            viewModel.updateReminder(reminderEntity)
        }
        adapter.clickListener.onItemClickIsMon = {reminderEntity ->
            reminderEntity.isMonday = !reminderEntity.isMonday
            viewModel.updateReminder(reminderEntity)
        }
        adapter.clickListener.onItemClickIsTue = {reminderEntity ->
            reminderEntity.isTuesday = !reminderEntity.isTuesday
            viewModel.updateReminder(reminderEntity)
        }
        adapter.clickListener.onItemClickIsWed = {reminderEntity ->
            reminderEntity.isWednesday = !reminderEntity.isWednesday
            viewModel.updateReminder(reminderEntity)
        }
        adapter.clickListener.onItemClickIsThu = { reminderEntity ->
            reminderEntity.isThursday = !reminderEntity.isThursday
            viewModel.updateReminder(reminderEntity)
        }
        adapter.clickListener.onItemClickIsFri = {reminderEntity ->
            reminderEntity.isFriday = !reminderEntity.isFriday
            viewModel.updateReminder(reminderEntity)
        }
        adapter.clickListener.onItemClickIsSat = {reminderEntity ->
            reminderEntity.isSaturday = !reminderEntity.isSaturday
            viewModel.updateReminder(reminderEntity)
        }
        adapter.clickListener.onItemClickIsSun = {reminderEntity ->
            reminderEntity.isSunday = !reminderEntity.isSunday

            viewModel.updateReminder(reminderEntity)

        }

        adapter.clickListener.onDeleteClick = { reminderEntity ->
            viewModel.deleteReminder(reminderEntity.time)
        }

        adapter.clickListener.onItemClickSwitch = { reminderEntity ->
            println("AAA " +reminderEntity.isStarted )
            if(reminderEntity.isStarted==true){
                reminderEntity.cancelAlarm(this)
                viewModel.updateReminder(reminderEntity.copy(isStarted =false))
                observe()
            }else{
                reminderEntity.schedule(this)
                viewModel.updateReminder(reminderEntity.copy(isStarted =true))
                observe()
            }
        }

        dialogTimePicker()
        dialogTimePicker2()


    }

    private fun dialogTimePicker() {

        val timePicker = TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->

            calendar.set(Calendar.HOUR_OF_DAY,hourOfDay)
            calendar.set(Calendar.MINUTE,minute)
            calendar.timeZone = TimeZone.getDefault()

            updateTimeLabel(calendar)
        }

        binding.fl1.setOnClickListener {
            TimePickerDialog(this,timePicker,calendar.get(Calendar.HOUR_OF_DAY)
                ,calendar.get(Calendar.MINUTE),false).show()
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun updateTimeLabel(calendar: Calendar) {
        val format = SimpleDateFormat("hh:mm aa")
        val time = format.format(calendar.time)
        val newReminder = ReminderEntity(time = time)
        viewModel.insertReminder(newReminder)
    }

    @SuppressLint("SimpleDateFormat")
    private fun dialogTimePicker2() {

        adapter.clickListener.onItemClickClock = {
            val timeNumbers = convertTimeStringToNumbers(it.time)

            val timePicker = TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->

                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                calendar.timeZone = TimeZone.getDefault()

                val format = SimpleDateFormat("hh:mm aa")
                val time = format.format(calendar.time)
                it.time = time
                viewModel.updateReminder(it)

            }
            if (timeNumbers != null) {
                val hour = timeNumbers.first
                val minute = timeNumbers.second
                TimePickerDialog(this, timePicker, hour, minute, false).show()
            }else{
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                TimePickerDialog(this, timePicker, hour, minute, false).show()
            }



        }
    }



    private fun generateReminders(wakeUpTimeHour: Int, wakeUpTimeMinute: Int, sleepTimeHour: Int, sleepTimeMinute: Int) {

       val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, wakeUpTimeHour)
        calendar.set(Calendar.MINUTE, wakeUpTimeMinute)

        val sleepCalendar = Calendar.getInstance()
        sleepCalendar.set(Calendar.HOUR_OF_DAY, sleepTimeHour)
        sleepCalendar.set(Calendar.MINUTE, sleepTimeMinute)


        // Cộng 1 giờ 30 phút cho mỗi lần cho đến khi thời gian lớn hơn thời gian ngủ
        while (calendar.timeInMillis <= sleepCalendar.timeInMillis) {

            val timeFormat = SimpleDateFormat("hh:mm aa")
            val reminderTime = timeFormat.format(calendar.time)
            val reminderEntity = ReminderEntity(time = reminderTime)

            reminderList.add(reminderEntity)
            viewModel.insertReminder(reminderEntity)
            calendar.add(Calendar.HOUR_OF_DAY, 1)
            calendar.add(Calendar.MINUTE, 30)
//            reminderEntity.schedule(this)
//            viewModel.updateReminder(reminderEntity)
        }


   }

    fun convertTimeStringToNumbers(timeString: String): Pair<Int, Int>? {
        val format = SimpleDateFormat("hh:mm aa", Locale.getDefault())
        try {
            val calendar = Calendar.getInstance()
            calendar.time = format.parse(timeString)
            val hour = calendar.get(Calendar.HOUR)
            val minute = calendar.get(Calendar.MINUTE)
            return Pair(hour, minute)
        } catch (e: ParseException) {
            return null
        }
    }
    private fun animateClock() {
//        val rotateAnimation = ObjectAnimator.ofFloat(binding., "rotation", 0f, 20f, 0f, -20f, 0f)
//        rotateAnimation.repeatCount = ValueAnimator.INFINITE
//        rotateAnimation.duration = 800
//        rotateAnimation.start()
    }


}