package com.example.waterreminder.ui.composite_screen.settings.sound.soundFragment.soundapp

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.waterreminder.R
import com.example.waterreminder.databinding.LayoutSoundAppBinding
import com.example.waterreminder.helper.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SoundAppFragment:Fragment() {
    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    @Inject
    lateinit var adapter: SoundAppAdapter

    private lateinit var binding: LayoutSoundAppBinding

    private val soundAppLiveData = MutableLiveData<List<SoundModel>>()
    private var isInitialized = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutSoundAppBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        setListener()
    }


    private fun initAdapter() {
       val sound =  preferenceHelper.getString(PreferenceHelper.PREF_SOUND)

        soundAppLiveData.value = mutableListOf(
            SoundModel("Classic", R.raw.classic),
            SoundModel("Drop echo",R.raw.drop_echo),
            SoundModel("Water drop",R.raw.water_drop),
            SoundModel("Water flow",R.raw.water_flow)
        )

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rclSoundApp.layoutManager = layoutManager
        binding.rclSoundApp.adapter = adapter

        soundAppLiveData.observe(viewLifecycleOwner) { sounds ->
            if(isInitialized){
                val updatedList = mutableListOf<SoundModel>()
                    val soundInt = sound?.toIntOrNull()
                    for (a in sounds) {
                        a.isCheck = soundInt != null && a.file == soundInt

                        updatedList.add(a)
                    }
                isInitialized = false
                adapter.submitList(updatedList)
            }else{
                adapter.submitList(sounds)
            }
        }

    }

    private fun setListener() {
        adapter.clickListener.onItemClick = { soundPhone ->
            preferenceHelper.setString(PreferenceHelper.PREF_SOUND,soundPhone.file.toString())
            soundAppLiveData.value = soundAppLiveData.value

        }
    }

}