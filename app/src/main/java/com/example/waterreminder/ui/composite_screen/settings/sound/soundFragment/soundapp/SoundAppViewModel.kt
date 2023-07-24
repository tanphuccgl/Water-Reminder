//package com.example.waterreminder.ui.composite_screen.settings.sound.soundFragment.soundapp
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.waterreminder.database.sound.SoundEntity
//import com.example.waterreminder.repository.SoundRepository
//import dagger.hilt.android.AndroidEntryPoint
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//@HiltViewModel
//class SoundAppViewModel @Inject constructor(
//    private val soundRepository: SoundRepository
//): ViewModel(){
//    val listSoundApp : LiveData<List<SoundEntity>>
//        get() = _listSoundApp
//    private val _listSoundApp = MutableLiveData<List<SoundEntity>>()
//
//    fun getSoundApp(){
//        viewModelScope.launch {
//            val sound = soundRepository.getSoundApp()
//            _listSoundApp.postValue(sound)
//        }
//    }
//
//    fun insertSound(soundEntity: SoundEntity) {
//        viewModelScope.launch(Dispatchers.IO) {
//            soundRepository.insertSound(soundEntity)
//            getSoundApp()
//        }
//    }
//
//    fun updateSound(soundEntity: SoundEntity) {
//        viewModelScope.launch(Dispatchers.IO) {
//            soundRepository.updateSound(soundEntity)
//            getSoundApp()
//        }
//    }
//}