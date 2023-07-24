package com.example.waterreminder.ui.dailywater.dailywaterfragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.waterreminder.databinding.LayoutDrinkMuchBinding
import com.example.waterreminder.helper.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DrinkMuchWaterFragment :Fragment(){

    @Inject
    lateinit var preferenceHelper: PreferenceHelper


    private lateinit var binding: LayoutDrinkMuchBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutDrinkMuchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {

        var quantityWater = preferenceHelper.getInt(PreferenceHelper.PREF_QUANTITY_WATER) ?: 0

        val weight = preferenceHelper.getInt(PreferenceHelper.PREF_WEIGHT)

        if(weight < 40){
            binding.tvTimes.text = "9 times a day"
            val count = (weight * 2 * 0.5 * 30)/9
            quantityWater = count.toInt()
            binding.tvMl.text = "$quantityWater ml each time"

            preferenceHelper.setInt(PreferenceHelper.PREF_QUANTITY_WATER,quantityWater)

        }else{
            binding.tvTimes.text = "12 times a day"
            val count = (weight * 2 * 0.5 * 30)/12
            quantityWater = count.toInt()
            binding.tvMl.text = "$quantityWater ml each time"

            preferenceHelper.setInt(PreferenceHelper.PREF_QUANTITY_WATER,quantityWater)

        }
    }

}