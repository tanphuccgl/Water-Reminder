package com.example.waterreminder.ui.question

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.waterreminder.ui.reminder.Reminder1Fragment
import com.example.waterreminder.ui.reminder.Reminder2Fragment
import com.example.waterreminder.ui.reminder.Reminder3Fragment
import com.example.waterreminder.ui.reminder.Reminder4Fragment

class QuestionsAdapter(fm:FragmentActivity) :  FragmentStateAdapter(fm) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> Reminder1Fragment()
            1 -> Reminder2Fragment()
            2 -> Reminder3Fragment()
            3 -> Reminder4Fragment()
            else -> Reminder1Fragment()
        }
    }
}
