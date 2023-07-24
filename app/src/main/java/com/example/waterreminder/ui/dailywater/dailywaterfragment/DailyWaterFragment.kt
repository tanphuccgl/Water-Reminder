package com.example.waterreminder.ui.dailywater.dailywaterfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.waterreminder.R
import com.example.waterreminder.databinding.LayoutDailyBinding
import com.example.waterreminder.helper.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DailyWaterFragment : Fragment(){

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    private lateinit var binding: LayoutDailyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutDailyBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

        val gender = preferenceHelper.getString(PreferenceHelper.PREF_SELECTED_GENDER)
        if (gender == "Male"){
            binding.imgGender.setImageResource(R.drawable.ic_male)
        }else{
            binding.imgGender.setImageResource(R.drawable.ic_female)
        }

        val dailyWater: Int

        val weight = preferenceHelper.getInt(PreferenceHelper.PREF_WEIGHT)

        val sum = weight*2*0.5*30
        dailyWater = sum.toInt()

        binding.tvWater.text = "$dailyWater"

        preferenceHelper.setInt(PreferenceHelper.PREF_DAILY_WATER,dailyWater)

    }
}