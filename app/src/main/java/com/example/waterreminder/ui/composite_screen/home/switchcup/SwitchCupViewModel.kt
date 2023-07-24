package com.example.waterreminder.ui.composite_screen.home.switchcup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waterreminder.database.WaterReminderEntity
import com.example.waterreminder.database.cup.SwitchCup
import com.example.waterreminder.repository.SwitchCupRepository
import com.example.waterreminder.repository.WaterReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SwitchCupViewModel @Inject constructor(
    private val switchCupRepository: SwitchCupRepository
): ViewModel() {


    val listSwitchCup: LiveData<List<SwitchCup>>
        get() = _listSwitchCup
    private val _listSwitchCup = MutableLiveData<List<SwitchCup>>()

    fun getSwitchCup() {
        viewModelScope.launch {
            val waterSwitchCup = switchCupRepository.getSwitchCup()
            _listSwitchCup.postValue(waterSwitchCup)
        }
    }

    fun insertSwitchCup(switchCup: SwitchCup) {
        viewModelScope.launch(Dispatchers.IO) {
            switchCupRepository.insertSwitchCup(switchCup)
            getSwitchCup()
        }
    }

    fun deleteSwitchCup(quantity:String){
        viewModelScope.launch(Dispatchers.IO) {
            switchCupRepository.deleteSwitchCup(quantity)
            getSwitchCup()
        }
    }
    fun updateWaterSwitchCup(switchCup: SwitchCup) {
        viewModelScope.launch(Dispatchers.IO) {
            switchCupRepository.updateSwitchCup(switchCup)
            getSwitchCup()
        }
    }


}