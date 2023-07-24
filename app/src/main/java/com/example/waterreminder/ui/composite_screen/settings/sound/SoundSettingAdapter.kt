package com.example.waterreminder.ui.composite_screen.settings.sound

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.waterreminder.ui.composite_screen.settings.sound.soundFragment.soundapp.SoundAppFragment
import com.example.waterreminder.ui.composite_screen.settings.sound.soundFragment.soundphone.SoundPhoneFragment

class SoundSettingAdapter(fm:FragmentActivity): FragmentStateAdapter(fm) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> SoundAppFragment()
            1 -> SoundPhoneFragment()
            else -> SoundAppFragment()
        }
    }

}