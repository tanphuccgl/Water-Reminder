package com.example.waterreminder.ui.composite_screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waterreminder.database.WaterReminderEntity
import com.example.waterreminder.repository.WaterReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val waterReminderRepository: WaterReminderRepository
):ViewModel() {


    val listWaterReminder: LiveData<List<WaterReminderEntity>>
        get() = _listWaterReminder
    private val _listWaterReminder = MutableLiveData<List<WaterReminderEntity>>()

    fun getWaterReminder() {
        viewModelScope.launch {
            val waterReminders = waterReminderRepository.getWaterReminders()
            _listWaterReminder.postValue(waterReminders)
        }
    }

    fun insertWaterReminder(waterReminderEntity: WaterReminderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            waterReminderRepository.insertWaterReminder(waterReminderEntity)
            getWaterReminder()
        }
    }
    fun updateWaterReminder(waterReminderEntity: WaterReminderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            waterReminderRepository.updateWaterReminder(waterReminderEntity)
            getWaterReminder()
        }
    }

    fun deleteWaterReminder(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            waterReminderRepository.deleteWaterReminder(date)
            getWaterReminder()
        }
    }

}