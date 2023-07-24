package com.example.waterreminder.ui.composite_screen.settings.sound.soundFragment.soundphone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waterreminder.database.cup.SwitchCup
import com.example.waterreminder.database.sound.SoundEntity
import com.example.waterreminder.repository.SoundRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SoundPhoneViewModel @Inject constructor(
    private val soundRepository: SoundRepository
):ViewModel(){

    val listSound :LiveData<List<SoundEntity>>
        get() = _listSound
    private val _listSound = MutableLiveData<List<SoundEntity>>()


    fun getSound() {
        viewModelScope.launch {
            val sound = soundRepository.getSounds()
            _listSound.postValue(sound)
        }
    }

    fun insertSound(soundEntity: SoundEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            soundRepository.insertSound(soundEntity)
            getSound()
        }
    }

    fun updateSound(soundEntity: SoundEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            soundRepository.updateSound(soundEntity)
            getSound()
        }
    }


}