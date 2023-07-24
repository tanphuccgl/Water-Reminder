package com.example.waterreminder.ui.dailywater

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.waterreminder.ui.dailywater.dailywaterfragment.DailyWaterFragment
import com.example.waterreminder.ui.dailywater.dailywaterfragment.DrinkMuchWaterFragment

class DailyWaterAdapter(fm: FragmentActivity) :FragmentStateAdapter(fm){
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> DailyWaterFragment()
            1 -> DrinkMuchWaterFragment()
            else -> DailyWaterFragment()
        }
    }

}