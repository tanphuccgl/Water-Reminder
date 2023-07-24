package com.example.waterreminder.ui.composite_screen.settings.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waterreminder.database.reminder.ReminderEntity
import com.example.waterreminder.repository.ReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleSettingViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository
):ViewModel(){
    val listReminder: LiveData<List<ReminderEntity>>
        get() = _listReminder
    private val _listReminder = MutableLiveData<List<ReminderEntity>>()

    fun getReminders() {
        viewModelScope.launch {
            val waterReminders = reminderRepository.getReminders()
            _listReminder.postValue(waterReminders)
        }
    }

    fun getReminder() {
        viewModelScope.launch {
            reminderRepository.getReminders()
        }
    }

    fun insertReminder(reminderEntity: ReminderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            reminderRepository.insertReminder(reminderEntity)
            getReminders()
        }
    }
    fun updateReminder(reminderEntity: ReminderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            reminderRepository.updateReminder(reminderEntity)
            getReminders()
        }
    }

    fun deleteReminder(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            reminderRepository.deleteReminder(date)
            getReminders()
        }
    }
}