package com.example.waterreminder.ui.composite_screen.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waterreminder.database.WaterReminderEntity
import com.example.waterreminder.repository.WaterReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val waterReminderRepository: WaterReminderRepository)
    :ViewModel(){

    val listWaterReminder: LiveData<List<WaterReminderEntity>>
        get() = _listWaterReminder
    private val _listWaterReminder = MutableLiveData<List<WaterReminderEntity>>()

    fun getWaterReminder() {
        viewModelScope.launch {
            val waterReminders = waterReminderRepository.getWaterReminders()
            _listWaterReminder.postValue(waterReminders)
        }
    }
}